package com.transferapp.service;

import com.transferapp.dto.request.TransferRequest;
import com.transferapp.dto.response.PagedResponse;
import com.transferapp.dto.response.TransactionResponse;
import com.transferapp.dto.response.TransferResponse;
import com.transferapp.entity.Account;
import com.transferapp.entity.Transaction;
import com.transferapp.entity.User;
import com.transferapp.mapper.TransactionMapper;
import com.transferapp.mapper.TransferMapper;
import com.transferapp.repository.AccountRepository;
import com.transferapp.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service handling fund transfers and paginated transaction history.
 *
 * Manual mapping replaced with injected MapStruct mappers:
 *   TransferMapper     → Transaction + senderAccount  → TransferResponse
 *   TransactionMapper  → Transaction + viewerAccountId → TransactionResponse
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final AccountRepository     accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransferMapper        transferMapper;
    private final TransactionMapper     transactionMapper;

    @Transactional
    public TransferResponse transfer(Long userId, TransferRequest request) {
        Account senderAccount = resolveAccount(userId);

        Account receiverAccount = accountRepository
                .findByAccountNumber(request.getReceiverAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Receiver account not found: " + request.getReceiverAccountNumber()));

        if (senderAccount.getId().equals(receiverAccount.getId()))
            throw new IllegalArgumentException("You cannot transfer funds to your own account");

        BigDecimal amount = request.getAmount();
        if (senderAccount.getBalance().compareTo(amount) < 0)
            throw new IllegalArgumentException(
                    "Insufficient funds. Available balance: " + senderAccount.getBalance());

        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        receiverAccount.setBalance(receiverAccount.getBalance().add(amount));
        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        Transaction transaction = Transaction.builder()
                .senderAccount(senderAccount)
                .receiverAccount(receiverAccount)
                .amount(amount)
                .status("SUCCESS")
                .createdAt(LocalDateTime.now())
                .build();
        transactionRepository.save(transaction);

        log.info("Transfer: {} -> {} | Amount: {}",
                senderAccount.getAccountNumber(), receiverAccount.getAccountNumber(), amount);

        return transferMapper.toResponse(transaction, senderAccount);
    }

    @Transactional(readOnly = true)
    public PagedResponse<TransactionResponse> getTransactionHistory(Long userId, int page, int size) {
        Account account = resolveAccount(userId);
        return buildPagedResponse(
                transactionRepository.findAllByAccountIdPaged(account.getId(), PageRequest.of(page, size)),
                account.getId());
    }

    @Transactional(readOnly = true)
    public PagedResponse<TransactionResponse> getSentTransactions(Long userId, int page, int size) {
        Account account = resolveAccount(userId);
        return buildPagedResponse(
                transactionRepository.findSentByAccountId(account.getId(), PageRequest.of(page, size)),
                account.getId());
    }

    @Transactional(readOnly = true)
    public PagedResponse<TransactionResponse> getReceivedTransactions(Long userId, int page, int size) {
        Account account = resolveAccount(userId);
        return buildPagedResponse(
                transactionRepository.findReceivedByAccountId(account.getId(), PageRequest.of(page, size)),
                account.getId());
    }

    private Account resolveAccount(Long userId) {
        return accountRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "No account found for user id: " + userId));
    }

    private PagedResponse<TransactionResponse> buildPagedResponse(
            Page<Transaction> page, Long viewerAccountId) {

        List<TransactionResponse> content = page.getContent().stream()
                .map(tx -> transactionMapper.toResponse(tx, viewerAccountId))
                .toList();

        return PagedResponse.<TransactionResponse>builder()
                .content(content)
                .currentPage(page.getNumber())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .pageSize(page.getSize())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
}

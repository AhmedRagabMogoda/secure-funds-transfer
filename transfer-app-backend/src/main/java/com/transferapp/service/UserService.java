package com.transferapp.service;

import com.transferapp.dto.response.UserProfileResponse;
import com.transferapp.entity.Account;
import com.transferapp.entity.User;
import com.transferapp.repository.AccountRepository;
import com.transferapp.repository.TransactionRepository;
import com.transferapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service handling user profile operations.
 * Aggregates user data, account data, and transaction statistics
 * into a single UserProfileResponse for the frontend profile page.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(Long userId) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("No account found for user id: " + userId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userId));

        int totalCount = transactionRepository.findAllByAccountId(account.getId()).size();
        BigDecimal totalSent = transactionRepository.sumSentByAccountId(account.getId());
        BigDecimal totalReceived = transactionRepository.sumReceivedByAccountId(account.getId());

        return UserProfileResponse.builder()
                .username(user.getUsername())
                .role(user.getRole().name())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .memberSince(user.getCreatedAt())
                .totalTransactionCount(totalCount)
                .totalSent(totalSent)
                .totalReceived(totalReceived)
                .build();
    }
}

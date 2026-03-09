package com.transferapp.service;

import com.transferapp.dto.response.AccountResponse;
import com.transferapp.entity.Account;
import com.transferapp.entity.User;
import com.transferapp.mapper.AccountMapper;
import com.transferapp.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service handling account retrieval.
 * Manual mapping replaced with the injected AccountMapper (MapStruct).
 */
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper     accountMapper;

    public AccountResponse getMyAccountById(Long userId) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("No account found for user id: " + userId));

        return accountMapper.toResponse(account);
    }
}

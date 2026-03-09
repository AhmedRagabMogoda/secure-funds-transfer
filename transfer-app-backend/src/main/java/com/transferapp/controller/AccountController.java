package com.transferapp.controller;

import com.transferapp.dto.response.AccountResponse;
import com.transferapp.dto.response.ApiResponse;
import com.transferapp.security.UserPrincipal;
import com.transferapp.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller exposing account-related endpoints.
 *
 * All endpoints require a valid JWT (enforced by SecurityConfig).
 * The authenticated user is injected via @AuthenticationPrincipal —
 * no need to parse the token manually in the controller.
 *
 * GET /api/accounts/me → returns the authenticated user's account details
 */
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * Returns the account details for the currently authenticated user.
     *
     * @param user injected from the SecurityContext by Spring Security
     * @return 200 OK with account number, balance, and owner username
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AccountResponse>> getMyAccount(
            @AuthenticationPrincipal UserPrincipal user) {


        AccountResponse account = accountService.getMyAccountById(user.getId());

        return ResponseEntity.ok(
                ApiResponse.success("Account retrieved successfully", account)
        );
    }
}

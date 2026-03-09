package com.transferapp.controller;

import com.transferapp.dto.request.TransferRequest;
import com.transferapp.dto.response.ApiResponse;
import com.transferapp.dto.response.PagedResponse;
import com.transferapp.dto.response.TransactionResponse;
import com.transferapp.dto.response.TransferResponse;
import com.transferapp.entity.User;
import com.transferapp.security.UserPrincipal;
import com.transferapp.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller exposing fund transfer and paginated transaction history endpoints.
 *
 * POST /api/transfers                  → execute a fund transfer
 * GET  /api/transfers/history          → all transactions (paginated)
 * GET  /api/transfers/history/sent     → sent only (paginated)
 * GET  /api/transfers/history/received → received only (paginated)
 */
@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<ApiResponse<TransferResponse>> transfer(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody TransferRequest request) {

        TransferResponse response = transferService.transfer(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.success("Transfer completed successfully", response));
    }

    /**
     * All transactions — paginated.
     * Query params: page (default 0), size (default 10)
     */
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<PagedResponse<TransactionResponse>>> getHistory(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PagedResponse<TransactionResponse> history = transferService.getTransactionHistory(user.getId(), page, size);
        return ResponseEntity.ok(ApiResponse.success("Transaction history retrieved", history));
    }

    /**
     * Sent transactions only — paginated.
     */
    @GetMapping("/history/sent")
    public ResponseEntity<ApiResponse<PagedResponse<TransactionResponse>>> getSent(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PagedResponse<TransactionResponse> history = transferService.getSentTransactions(user.getId(), page, size);
        return ResponseEntity.ok(ApiResponse.success("Sent transactions retrieved", history));
    }

    /**
     * Received transactions only — paginated.
     */
    @GetMapping("/history/received")
    public ResponseEntity<ApiResponse<PagedResponse<TransactionResponse>>> getReceived(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PagedResponse<TransactionResponse> history = transferService.getReceivedTransactions(user.getId(), page, size);
        return ResponseEntity.ok(ApiResponse.success("Received transactions retrieved", history));
    }
}

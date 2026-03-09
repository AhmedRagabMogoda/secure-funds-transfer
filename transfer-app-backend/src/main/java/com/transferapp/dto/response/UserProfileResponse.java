package com.transferapp.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO returned when the authenticated user requests their profile information.
 * Exposes safe fields only — password and internal IDs are never included.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse {

    private String username;
    private String role;
    private String accountNumber;
    private BigDecimal balance;
    private LocalDateTime memberSince;

    // Transfer statistics
    private int totalTransactionCount;
    private BigDecimal totalSent;
    private BigDecimal totalReceived;
}

package com.transferapp.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO returned when exposing account information to the client.
 * Prevents exposing the Account entity directly and hides internal IDs.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {

    private String accountNumber;
    private BigDecimal balance;
    private String ownerUsername;
    private LocalDateTime createdAt;
}

package com.transferapp.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO returned after a successful or failed fund transfer operation.
 * Provides the client with a complete picture of the transaction result.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferResponse {

    private String senderAccountNumber;
    private String receiverAccountNumber;
    private BigDecimal amount;
    private BigDecimal newBalance;
    private String status;
    private LocalDateTime createdAt;
}

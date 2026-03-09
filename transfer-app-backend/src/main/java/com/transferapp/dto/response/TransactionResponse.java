package com.transferapp.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO representing a single transaction record in the user's history.
 * Exposes account numbers rather than internal IDs.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {

    private Long id;
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt;

    /**
     * Indicates the direction of this transaction from the viewer's perspective.
     * "SENT" if the viewer is the sender, "RECEIVED" if the viewer is the receiver.
     */
    private String direction;
}

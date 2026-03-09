package com.transferapp.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown by TransferService when the sender's account balance
 * is less than the requested transfer amount.
 *
 * Mapped to HTTP 400 Bad Request by GlobalExceptionHandler.
 */
public class InsufficientFundsException extends BaseException {

    public InsufficientFundsException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "INSUFFICIENT_FUNDS");
    }

    public InsufficientFundsException() {
        super("Insufficient funds to complete this transfer", HttpStatus.BAD_REQUEST, "INSUFFICIENT_FUNDS");
    }
}

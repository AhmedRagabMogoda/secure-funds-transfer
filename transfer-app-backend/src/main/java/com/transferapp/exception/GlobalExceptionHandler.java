package com.transferapp.exception;

import com.transferapp.dto.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

/**
 * Centralized exception handler for all controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle all custom base exceptions
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleBaseException(BaseException ex, WebRequest request)
    {
        log.error("Base exception occurred: {}", ex.getMessage());

        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        ApiResponse<ErrorDetails> response = ApiResponse.error(ex.getMessage(), errorDetails);

        return new ResponseEntity<> (response, ex.getStatus());
    }

    /**
     * Handles insufficient funds during a transfer — returns 400 Bad Request.
     */
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleInsufficientFunds(InsufficientFundsException ex, WebRequest request) {
        log.error("Transfer rejected — insufficient funds: {}", ex.getMessage());

        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .path(request.getDescription(false).replace("url=", ""))
                .build();
        ApiResponse<ErrorDetails> response = ApiResponse.error(ex.getMessage(), errorDetails);

        return new ResponseEntity<> (response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle ResourceNotFoundException
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        log.error("Resource Not Found Exception occured: {}", ex.getMessage());

        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .path(request.getDescription(false).replace("url=", ""))
                .build();
        ApiResponse<ErrorDetails> response = ApiResponse.error(ex.getMessage(), errorDetails);

        return new ResponseEntity<> (response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleUnauthorizedException(UnauthorizedException ex, WebRequest request)
    {
        log.error("Unauthorized access: {}", ex.getMessage());

        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        ApiResponse<ErrorDetails> response = ApiResponse.error(ex.getMessage(), errorDetails);

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

}

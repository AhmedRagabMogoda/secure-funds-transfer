package com.transferapp.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorDetails {

    private LocalDateTime timestamp;

    private String errorCode;

    private String message;

    private String path;
}

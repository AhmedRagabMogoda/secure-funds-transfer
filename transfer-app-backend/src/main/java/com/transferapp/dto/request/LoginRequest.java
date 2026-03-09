package com.transferapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO for the login request body.
 * Received at POST /api/auth/login
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}

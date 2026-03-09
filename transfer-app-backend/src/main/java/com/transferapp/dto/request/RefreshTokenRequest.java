package com.transferapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO for the refresh token request body.
 * Received at POST /api/auth/refresh
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenRequest {

    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}

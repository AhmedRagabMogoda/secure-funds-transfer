package com.transferapp.controller;

import com.transferapp.dto.response.AuthResponse;
import com.transferapp.dto.request.LoginRequest;
import com.transferapp.dto.request.RefreshTokenRequest;
import com.transferapp.dto.response.ApiResponse;
import com.transferapp.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller exposing authentication endpoints.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Authenticates a user and issues JWT tokens.
     *
     * @param request contains username and password
     * @return 200 OK with accessToken, refreshToken, and role
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {

        AuthResponse authResponse = authService.login(request);

        return ResponseEntity.ok(ApiResponse.success("Login successful", authResponse));
    }

    /**
     * Issues a new access token using a valid refresh token.
     * Called automatically by the Angular interceptor when the access token expires.
     *
     * @param request contains the refresh token string
     * @return 200 OK with a new access token and the existing refresh token
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@Valid @RequestBody RefreshTokenRequest request) {

        AuthResponse authResponse = authService.refreshToken(request);

        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", authResponse));
    }
}

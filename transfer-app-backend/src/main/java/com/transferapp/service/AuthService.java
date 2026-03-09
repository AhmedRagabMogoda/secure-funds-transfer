package com.transferapp.service;

import com.transferapp.dto.request.LoginRequest;
import com.transferapp.dto.request.RefreshTokenRequest;
import com.transferapp.dto.response.AuthResponse;
import com.transferapp.entity.User;
import com.transferapp.exception.ResourceNotFoundException;
import com.transferapp.exception.UnauthorizedException;
import com.transferapp.repository.UserRepository;
import com.transferapp.security.JwtService;
import com.transferapp.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("User attempting to login: {}", request.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            User user = userRepository.findById(userPrincipal.getId())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userPrincipal.getId()));

            userRepository.updateLastLoginAt(userPrincipal.getId(), LocalDateTime.now());

            String accessToken = jwtService.generateToken(userPrincipal, userPrincipal.getId());
            String refreshToken = jwtService.generateRefreshToken(userPrincipal, userPrincipal.getId());

            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .role(user.getRole().name())
                    .build();

        } catch(BadCredentialsException e) {
            log.error("Login failed for user: {}", request.getUsername());
            throw new UnauthorizedException("Invalid username or password");
        }
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        log.info("Refreshing access token");

        String refreshToken = request.getRefreshToken();
        if(!jwtService.validateToken(refreshToken)) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }

        String username = jwtService.extractUsername(refreshToken);
        Long userId = jwtService.extractUserId(refreshToken);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        UserPrincipal userPrincipal = UserPrincipal.create(user);

        String newAccessToken = jwtService.generateToken(userPrincipal, userId);
        String newRefreshToken = jwtService.generateRefreshToken(userPrincipal, user.getId());

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .role(user.getRole().name())
                .build();
    }
}
package com.transferapp.controller;


import com.transferapp.dto.response.ApiResponse;
import com.transferapp.dto.response.UserProfileResponse;
import com.transferapp.entity.User;
import com.transferapp.security.UserPrincipal;
import com.transferapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller exposing user profile endpoints.
 *
 * GET /api/users/profile → returns full profile with stats for the authenticated user
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(
            @AuthenticationPrincipal UserPrincipal user) {

        UserProfileResponse profile = userService.getProfile(user.getId());
        return ResponseEntity.ok(ApiResponse.success("Profile retrieved successfully", profile));
    }
}

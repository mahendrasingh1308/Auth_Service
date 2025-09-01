package com.auth.service.controller;

import com.auth.service.dto.*;
import com.auth.service.service.AuthService;
import com.auth.service.util.AppConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller for handling user signup, login, refresh, and logout.
 * <p>
 * Supports both Fan and Creator registration flows, JWT login/refresh, and secure logout.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Register a new Fan account.
     *
     * @param request request payload containing Fan signup details
     * @return success response with Fan details
     */
    @PostMapping("/fan/signup")
    public ResponseEntity<ApplicationResponse<FanSignupResponse>> registerFan(@RequestBody FanSignupRequest request) {
        FanSignupResponse response = authService.registerFan(request);
        return ResponseEntity.ok(
                new ApplicationResponse<>(200, AppConstants.SIGNUP_SUCCESS, response)
        );
    }

    /**
     * Register a new Creator account.
     *
     * @param request request payload containing Creator signup details
     * @return success response with Creator details
     */
    @PostMapping("/creator/signup")
    public ResponseEntity<ApplicationResponse<CreatorSignupResponse>> registerCreator(@RequestBody CreatorSignupRequest request) {
        CreatorSignupResponse response = authService.registerCreator(request);
        return ResponseEntity.ok(
                new ApplicationResponse<>(200, AppConstants.SIGNUP_SUCCESS, response)
        );
    }

    /**
     * Login with email/username/phone and password.
     *
     * @param request login credentials
     * @return JWT access and refresh tokens
     */
    @PostMapping("/login")
    public ResponseEntity<ApplicationResponse<JwtResponse>> login(@RequestBody LoginRequest request) {
        JwtResponse jwtResponse = authService.login(request);
        return ResponseEntity.ok(
                new ApplicationResponse<>(200, AppConstants.LOGIN_SUCCESS, jwtResponse)
        );
    }

    /**
     * Refresh JWT tokens using a valid refresh token.
     *
     * @param request contains refresh token
     * @return new JWT access and refresh tokens
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApplicationResponse<JwtResponse>> refreshToken(@RequestBody TokenRefreshRequest request) {
        JwtResponse jwtResponse = authService.refreshToken(request);
        return ResponseEntity.ok(
                new ApplicationResponse<>(200, AppConstants.TOKEN_REFRESHED, jwtResponse)
        );
    }

    /**
     * Logout user by invalidating the refresh token.
     * Refresh token must be provided in the Authorization header as "Bearer <token>".
     *
     * @param request HTTP request containing Authorization header
     * @return success message if logout is successful
     */
    @PostMapping("/logout")
    public ResponseEntity<ApplicationResponse<String>> logout(HttpServletRequest request) {
        String header = request.getHeader(AppConstants.AUTHORIZATION);
        if (header == null || !header.startsWith(AppConstants.BEARER)) {
            return ResponseEntity.badRequest()
                    .body(new ApplicationResponse<>(400, AppConstants.REFRESH_TOKEN_MISSING, null));
        }

        String refreshToken = header.substring(7);
        authService.logout(refreshToken);

        return ResponseEntity.ok(
                new ApplicationResponse<>(200, AppConstants.LOGOUT_SUCCESS, "Logged out successful")
        );
    }
}

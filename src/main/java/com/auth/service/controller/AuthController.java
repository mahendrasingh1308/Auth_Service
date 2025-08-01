package com.auth.service.controller;

import com.auth.service.dto.*;
import com.auth.service.entity.UserCredential;
import com.auth.service.jwt.JwtUtil;
import com.auth.service.service.AuthService;
import com.auth.service.service.OtplessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.auth.service.util.AppConstants.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final OtplessService otplessService;
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    /**
     * Register a new user.
     */
    @PostMapping("/signup")
    public ResponseEntity<ApplicationResponse<SignupResponse>> signup(@RequestBody SignupRequest request) {
        SignupResponse response = authService.signup(request);
        return ResponseEntity.ok(
                ApplicationResponse.<SignupResponse>builder()
                        .statusCode(200)
                        .payload(response)
                        .message(response.getMessage())
                        .build()
        );
    }

    /**
     * Login endpoint - returns access & refresh tokens.
     */
    @PostMapping("/login")
    public ResponseEntity<ApplicationResponse<JwtResponse>> login(@RequestBody LoginRequest request) {
        JwtResponse response = authService.login(request);
        return ResponseEntity.ok(
                ApplicationResponse.<JwtResponse>builder()
                        .statusCode(200)
                        .payload(response)
                        .message(USER_LOGGED_IN_SUCCESSFULLY)
                        .build()
        );
    }

    /**
     * Refresh access token using refresh token.
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApplicationResponse<JwtResponse>> refreshToken(@RequestBody TokenRefreshRequest request) {
        JwtResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(
                ApplicationResponse.<JwtResponse>builder()
                        .statusCode(200)
                        .payload(response)
                        .message(TOKEN_REFERESED_SUCCESSFULLY)
                        .build()
        );
    }

    /**
     * OTPLESS login or registration endpoint
     */
    @PostMapping("/otpless/callback")
    public ResponseEntity<ApplicationResponse<JwtResponse>> otplessCallback(@RequestParam("token") String token) {
        OtplessUser otplessUser = otplessService.verifyOtplessToken(token);

        // Register or login user
        UserCredential userCredential = authService.registerOrLoginWithOtpless(otplessUser);

        // Generate tokens
        String accessToken = jwtUtil.generateAccessToken(userCredential);
        String refreshToken = jwtUtil.generateRefreshToken(userCredential);

        JwtResponse jwtResponse = new JwtResponse(accessToken, refreshToken, userCredential.getUuid());

        return ResponseEntity.ok(
                ApplicationResponse.<JwtResponse>builder()
                        .statusCode(200)
                        .payload(jwtResponse)
                        .message(OTPLESS_LOGIN_SUCCESS)
                        .build()
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApplicationResponse<String>> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String message = authService.logout(token);

        return ResponseEntity.ok(
                ApplicationResponse.<String>builder()
                        .statusCode(200)
                        .message(message)
                        .payload(message)
                        .build()
        );
    }
}

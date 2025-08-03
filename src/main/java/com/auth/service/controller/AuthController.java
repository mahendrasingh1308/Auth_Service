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

/**
 * REST controller for handling authentication operations.
 * <p>
 * Exposes endpoints for:
 * - User signup
 * - User login
 * - Token refresh
 * - Otpless login (via WhatsApp)
 * - Logout
 * </p>
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final OtplessService otplessService;
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    /**
     * Registers a new user with provided credentials.
     *
     * @param request the signup request containing user details
     * @return response containing user info and success message
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
     * Authenticates a user and returns access and refresh JWT tokens.
     *
     * @param request the login request containing credentials
     * @return response with generated tokens and user information
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
     * Generates a new access token using a valid refresh token.
     *
     * @param request the token refresh request containing refresh token
     * @return response with new access and refresh tokens
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
     * Handles OTP-less login or registration via Otpless integration.
     * Accepts token received via Otpless redirect and returns JWT tokens.
     *
     * @param token the Otpless identity token
     * @return response with tokens for the authenticated user
     */
    @PostMapping("/otpless/callback")
    public ResponseEntity<ApplicationResponse<JwtResponse>> otplessCallback(@RequestParam("token") String token) {
        OtplessUser otplessUser = otplessService.verifyOtplessToken(token);

        UserCredential userCredential = authService.registerOrLoginWithOtpless(otplessUser);

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

    /**
     * Logs out the user by invalidating the access token.
     *
     * @param authHeader the Authorization header containing the Bearer token
     * @return response confirming logout
     */
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






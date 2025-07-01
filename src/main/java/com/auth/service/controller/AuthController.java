package com.auth.service.controller;

import com.auth.service.dto.*;
import com.auth.service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.auth.service.util.AppConstants.*;

/**
 * REST controller for handling authentication-related operations like
 * signup, login, token refresh, and profile retrieval.
 * <p>
 * Exposes endpoints under the base URL path <code>/api/auth</code>.
 * </p>
 *
 * @author Mahendra
 * @since 2025
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint for registering a new user.
     *
     * @param request Signup request DTO containing name, email, and password
     * @return {@link ApplicationResponse} with success message and payload
     */
    @PostMapping("/signup")
    public ResponseEntity<ApplicationResponse<SignupResponse>> signup(@RequestBody SignupRequest request) {
        SignupResponse response = authService.signup(request);
        return ResponseEntity.ok(
                ApplicationResponse.<SignupResponse>builder()
                        .statusCode(200)
                        .payload(response)
                        .message(USER_REGISTERED_SUCCESSFULLY)
                        .build()
        );
    }

    /**
     * Endpoint to authenticate user and return JWT access and refresh tokens.
     *
     * @param request Login request DTO with email and password
     * @return {@link ApplicationResponse} with token payload
     */
    @PostMapping("/login")
    public ResponseEntity<ApplicationResponse<JwtResponse>> login(@RequestBody LoginRequest request) {
        JwtResponse response = authService.login(request);
        return ResponseEntity.ok(
                ApplicationResponse.<JwtResponse>builder()
                        .statusCode(200)
                        .payload(response)
                        .message(USER_LOGEDIN_SUCCESSFULLTY)
                        .build()
        );
    }

    /**
     * Endpoint to generate a new access token using a valid refresh token.
     *
     * @param request Refresh token DTO
     * @return {@link ApplicationResponse} with new access token
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
     * Endpoint to retrieve currently logged-in user's profile info.
     *
     * @param authentication Spring Security's authentication object (auto injected)
     * @return {@link ApplicationResponse} containing user's email
     */
    @GetMapping("/profile")
    public ResponseEntity<ApplicationResponse<String>> getUserProfile(Authentication authentication) {
        return ResponseEntity.ok(
                ApplicationResponse.<String>builder()
                        .statusCode(200)
                        .payload(authentication.getName())
                        .message(PROFILE_ACCESS_GRANTED)
                        .build()
        );
    }
}

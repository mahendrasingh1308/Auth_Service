package com.auth.service.controller;

import com.auth.service.dto.*;
import com.auth.service.service.AuthService;
import com.auth.service.util.AppConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Controller for managing authentication-related operations.
 * <p>
 * Provides endpoints for:
 * <ul>
 *   <li>Fan and Creator registration</li>
 *   <li>Login and token refresh</li>
 *   <li>Logout</li>
 *   <li>Google OAuth2 login success</li>
 *   <li>Token blacklist check</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Registers a new Fan account.
     *
     * @param request the fan signup request payload
     * @return the fan signup response
     */
    @PostMapping("/fan/signup")
    public ResponseEntity<ApplicationResponse<FanSignupResponse>> registerFan(@RequestBody FanSignupRequest request) {
        FanSignupResponse response = authService.registerFan(request);
        return ResponseEntity.ok(
                new ApplicationResponse<>(200, AppConstants.SIGNUP_SUCCESS, response)
        );
    }

    /**
     * Registers a new Creator account.
     *
     * @param request the creator signup request payload
     * @return the creator signup response
     */
    @PostMapping("/creator/signup")
    public ResponseEntity<ApplicationResponse<CreatorSignupResponse>> registerCreator(@RequestBody CreatorSignupRequest request) {
        CreatorSignupResponse response = authService.registerCreator(request);
        return ResponseEntity.ok(
                new ApplicationResponse<>(200, AppConstants.SIGNUP_SUCCESS, response)
        );
    }

    /**
     * Authenticates a user with credentials (email/username/phone + password).
     *
     * @param request the login request payload
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
     * Refreshes JWT tokens using a valid refresh token.
     *
     * @param request the refresh token request payload
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
     * Logs out the user by invalidating the access token.
     * The token must be provided in the Authorization header as "Bearer &lt;token&gt;".
     *
     * @param request the HTTP request containing the Authorization header
     * @return a success response if logout is successful
     */
    @PostMapping("/logout")
    public ResponseEntity<ApplicationResponse<String>> logout(HttpServletRequest request) {
        String header = request.getHeader(AppConstants.AUTHORIZATION);

        if (header == null || !header.startsWith(AppConstants.BEARER)) {
            return ResponseEntity.badRequest()
                    .body(new ApplicationResponse<>(400, AppConstants.ACCESS_TOKEN_MISSING, null));
        }

        String accessToken = header.substring(AppConstants.BEARER.length()).trim();

        if (authService.isTokenBlacklisted(accessToken)) {
            return ResponseEntity.badRequest()
                    .body(new ApplicationResponse<>(400, AppConstants.TOKEN_BLACKLIST, null));
        }

        authService.logoutWithAccessToken(accessToken);

        return ResponseEntity.ok(
                new ApplicationResponse<>(200, AppConstants.LOGOUT_SUCCESS, AppConstants.LOGOUT_MESSAGE)
        );
    }

}

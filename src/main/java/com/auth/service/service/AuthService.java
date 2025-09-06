package com.auth.service.service;

import com.auth.service.dto.*;

/**
 * Service interface for handling authentication-related operations.
 * <p>
 * Provides methods for registering fans and creators, authenticating users,
 * refreshing tokens, logging out, and supporting Google login.
 */
public interface AuthService {

    /**
     * Registers a new fan in the system.
     *
     * @param request the fan signup request containing registration details
     * @return the response with fan information and status
     */
    FanSignupResponse registerFan(FanSignupRequest request);

    /**
     * Registers a new creator in the system.
     *
     * @param request the creator signup request containing registration details
     * @return the response with creator information and status
     */
    CreatorSignupResponse registerCreator(CreatorSignupRequest request);

    /**
     * Authenticates a user with email and password credentials.
     *
     * @param request the login request containing user credentials
     * @return a JWT response with access and refresh tokens
     */
    JwtResponse login(LoginRequest request);

    /**
     * Generates a new access token and refresh token pair
     * using a valid refresh token.
     *
     * @param request the token refresh request
     * @return a new JWT response with refreshed tokens
     */
    JwtResponse refreshToken(TokenRefreshRequest request);

    /**
     * Logs out a user by invalidating the given access token.
     *
     * @param accessToken the JWT access token to invalidate
     */
    void logoutWithAccessToken(String accessToken);
    /**
     * Check if token is blacklisted
     *
     * @param token JWT token
     * @return true if token is blacklisted
     */
    boolean isTokenBlacklisted(String token);
}

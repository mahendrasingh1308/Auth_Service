package com.auth.service.service;

import com.auth.service.dto.*;
import com.auth.service.entity.UserCredential;

/**
 * AuthService defines authentication-related operations such as
 * signup, login, token refresh, OTP-less login, and logout.
 */
public interface AuthService {

    /**
     * Registers a new user based on the signup request details.
     *
     * @param request the user's signup data
     * @return a response message indicating signup success or failure
     */
    SignupResponse signup(SignupRequest request);

    /**
     * Authenticates a user using email and password.
     *
     * @param request the login credentials
     * @return a JWT access and refresh token pair
     */
    JwtResponse login(LoginRequest request);

    /**
     * Generates a new access token using the provided refresh token.
     *
     * @param request the refresh token request
     * @return a new JWT access and refresh token
     */
    JwtResponse refreshToken(TokenRefreshRequest request);

    /**
     * Handles login or registration using OTP-less login mechanisms
     * such as WhatsApp, SMS, etc.
     *
     * @param otplessUser the user details from the OTP-less provider
     * @return the registered or logged-in user entity
     */
    UserCredential registerOrLoginWithOtpless(OtplessUser otplessUser);

    /**
     * Logs out the user and optionally invalidates the access token.
     *
     * @param token the JWT token to invalidate
     * @return a confirmation message of successful logout
     */
    String logout(String token);
}

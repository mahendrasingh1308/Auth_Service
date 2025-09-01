package com.auth.service.util;

/**
 * Application-wide constants for messages and statuses.
 * Centralized here to avoid hardcoding strings in controllers/services.
 */
public class AppConstants {

    // Signup
    public static final String SIGNUP_SUCCESS = "Signup successful";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String USERNAME_ALREADY_EXISTS = "Username already exists";
    public static final String PHONE_ALREADY_EXISTS = "Phone already exists";
    public static final String MOBILE_ALREADY_EXISTS = "Mobile number already exists";

    // Login
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    public static final String USER_NOT_FOUND = "User not found";

    // Token
    public static final String TOKEN_REFRESHED = "Token refreshed successfully";
    public static final String INVALID_REFRESH_TOKEN = "Invalid refresh token";
    public static final String REFRESH_TOKEN_MISSING = "Refresh token is missing";

    // Logout
    public static final String LOGOUT_SUCCESS = "Logout successful";
    public static final String  AUTHORIZATION="Authorization";
    public static final String  BEARER="Bearer";

    private AppConstants() {
        // prevent instantiation
    }
}

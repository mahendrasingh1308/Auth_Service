package com.auth.service.util;

public class AppConstants {

    // Existing constants (add these if missing)
    public static final String SIGNUP_SUCCESS = "Registered successfully";
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String TOKEN_REFRESHED = "Token refreshed successfully";
    public static final String LOGOUT_SUCCESS = "Logout successful";
    public static final String LOGOUT_MESSAGE = " Logged out successfully";

    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String USERNAME_ALREADY_EXISTS = "Username already exists";
    public static final String PHONE_ALREADY_EXISTS = "Phone already exists";
    public static final String MOBILE_ALREADY_EXISTS = "Mobile already exists";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    public static final String INVALID_REFRESH_TOKEN = "Invalid refresh token";
    public static final String TOKEN_BLACKLIST = "Token blacklisted";

    // Authorization constants
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    // 🔹 Google login messages

    // Token missing constants
    public static final String ACCESS_TOKEN_MISSING = "Access token is required in Authorization header";
    // Private constructor to prevent instantiation
    private AppConstants() {}
}
package com.auth.service.dto;

import lombok.Data;

/**
 * DTO representing the login request payload.
 * <p>
 * Used to authenticate a user with email and password.
 * </p>
 */
@Data
public class LoginRequest {

    /**
     * The email address used as the username for login.
     */
    private String email;

    /**
     * The user's password in plain text (will be encoded during processing).
     */
    private String password;
}

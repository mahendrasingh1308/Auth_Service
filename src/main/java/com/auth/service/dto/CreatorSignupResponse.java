package com.auth.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing the response returned after a successful
 * or failed creator signup attempt.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatorSignupResponse {

    /**
     * Unique identifier of the creator.
     */
    private String uuid;

    /**
     * Creator's chosen username.
     */
    private String username;

    /**
     * Full name of the creator.
     */
    private String fullName;

    /**
     * Response message (e.g., "Signup successful", "Email already exists").
     */
    private String message;
}

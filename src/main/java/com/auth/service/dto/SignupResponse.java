package com.auth.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO representing the response returned after a successful or failed signup attempt.
 * <p>
 * Contains only a message field to indicate the result of the operation.
 * </p>
 */
@Data
@AllArgsConstructor
public class SignupResponse {

    /**
     * Response message (e.g., "Signup successful", "Email already exists").
     */
    private String message;
}

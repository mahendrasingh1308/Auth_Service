package com.auth.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing the response returned after a successful or failed fan signup attempt.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FanSignupResponse {

    /**
     * Response message (e.g., "Signup successful", "Email already exists").
     */
    private String message;

    /**
     * Optional UUID assigned to the fan after successful signup.
     */
    private String uuid;

    /**
     * Username assigned to the fan.
     */
    private String username;
}

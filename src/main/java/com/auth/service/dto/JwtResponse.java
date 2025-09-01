package com.auth.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing the response after successful authentication.
 * <p>
 * Contains the JWT access token, refresh token, and user UUID.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    /**
     * The JWT access token used for authorizing API requests.
     */
    private String accessToken;

    /**
     * The JWT refresh token used to obtain a new access token when the current one expires.
     */
    private String refreshToken;

    /**
     * Unique identifier (UUID) of the authenticated user.
     */
    private String uuid;

    private String message;

}

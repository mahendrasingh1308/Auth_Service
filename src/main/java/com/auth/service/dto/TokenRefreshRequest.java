package com.auth.service.dto;

import lombok.Data;

/**
 * DTO representing the request to refresh an expired JWT access token.
 * <p>
 * Contains the refresh token required to generate a new access token.
 * </p>
 */
@Data
public class TokenRefreshRequest {

    /**
     * The refresh token issued during the initial authentication.
     */
    private String refreshToken;
}

package com.auth.service.dto;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken; // New request DTO
}

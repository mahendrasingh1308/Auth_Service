package com.auth.service.dto;

import lombok.Data;

@Data
public class OtplessUser {
    private String phone;
    private String email;
    private String channel;

}

package com.auth.service.dto;

import lombok.Data;

/**
 * DTO representing a user authenticated via Otpless (e.g., WhatsApp, SMS).
 * <p>
 * Contains minimal user information such as phone, email, and login channel.
 * </p>
 */
@Data
public class OtplessUser {

    /**
     * The phone number of the user.
     */
    private String phone;

    /**
     * The email address of the user (if available).
     */
    private String email;

    /**
     * The channel through which the user logged in (e.g., WHATSAPP, SMS, EMAIL).
     */
    private String channel;
}

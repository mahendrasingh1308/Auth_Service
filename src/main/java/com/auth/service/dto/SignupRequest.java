package com.auth.service.dto;

import lombok.Data;

/**
 * DTO for user signup request payload.
 * <p>
 * This object is used to capture the user's signup input data
 * such as name, email, password, and other identity details.
 * </p>
 */
@Data
public class SignupRequest {

    /**
     * User's first name.
     */
    private String firstName;

    /**
     * User's last name.
     */
    private String lastName;

    /**
     * User's email address.
     * <p>This will be used as the login identifier.</p>
     */
    private String email;

    /**
     * User's phone number.
     */
    private String phone;

    /**
     * User's account password.
     * <p>Optional for OAuth/OTP-less logins.</p>
     */
    private String password;

    /**
     * Role of the user (e.g., ADMIN, USER).
     */
    private String role;

    /**
     * Channel used for login (e.g., EMAIL, GOOGLE, WHATSAPP).
     */
    private String loginChannel;
}

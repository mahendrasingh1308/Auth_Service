package com.auth.service.dto;

import lombok.Data;

/**
 * DTO for creator signup request payload.
 * Captures both personal and billing information of a creator.
 */
@Data
public class CreatorSignupRequest {

    // Personal Information
    private String fullName;
    private String username;
    private String email;
    private String mobile;
    private String gender;

    /**
     * Date of birth (in "yyyy-MM-dd" format).
     * Example: "1995-08-15"
     */
    private String dateOfBirth;

    private String password;
    private String confirmPassword;
    private String bio;

    // Billing Information
    private String company;
    private String country;
    private String state;
    private String city;
    private String postalCode;
    private String billingAddress;

    // Social Media Links
    private String instagramUrl;
    private String facebookUrl;
    private String youtubeUrl;
    private String twitterUrl;
}

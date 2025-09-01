//package com.auth.service.dto;
//
//import lombok.Data;
//
///**
// * DTO for user signup request payload.
// * <p>
// * This object is used to capture the user's signup input data
// * such as name, email, password, and other identity details.
// * </p>
// */
//@Data
//public class SignupRequest {
//
//    /**
//     * User's first name.
//     */
//    private String firstName;
//
//    /**
//     * User's last name.
//     */
//    private String lastName;
//
//    /**
//     * User's email address.
//     * <p>This will be used as the login identifier.</p>
//     */
//    private String email;
//
//    /**
//     * User's phone number.
//     */
//    private String phone;
//
//    /**
//     * User's account password.
//     * <p>Optional for OAuth/OTP-less logins.</p>
//     */
//    private String password;
//
//    /**
//     * Field for confirming user's password during signup.
//     * <p>This is used only for validation and is not persisted.</p>
//     */
//    private String confirmPassword;
//
//    /**
//     * Role of the user (e.g., ADMIN, USER).
//     */
//    private String role;
//
//    /**
//     * Channel used for login (e.g., EMAIL, GOOGLE, WHATSAPP).
//     */
//    private String loginChannel;
//}
package com.auth.service.dto;

import lombok.Data;

/**
 * DTO for user signup request payload.
 * This object captures the user's signup input data
 * such as name, email, phone, password, and optional social links.
 */
@Data
public class FanSignupRequest {

    /**
     * Full name of the user
     */
    private String fullName;

    /**
     * Username (must be unique)
     */
    private String username;

    /**
     * User's email address
     */
    private String email;

    /**
     * User's phone number
     */
    private String phone;

    /**
     * Account password (optional for OAuth/OTP-less)
     */
    private String password;

    /**
     * Confirm password (not persisted, used only for validation)
     */
    private String confirmPassword;

    /**
     * User biography
     */
    private String bio;

    /**
     * Social links
     */
    private String instagramLink;
    private String facebookLink;
    private String youtubeLink;
    private String xUrl;
}

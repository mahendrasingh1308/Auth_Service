//package com.auth.service.entity;
//import com.auth.service.enums.Role;
//import com.auth.service.enums.LoginChannel;
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//import java.time.LocalDateTime;
//
///**
// * Entity class that represents the credentials of a user stored in the system.
// * This includes identity, login method, and role information.
// */
//@Entity
//@Table(name = "user_credentials")
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class UserCredential {
//
//    /**
//     * Primary key for the user_credentials table.
//     */
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    /**
//     * Unique user identifier used for communication acros s services.
//     */
//    @Column(name="uuid",nullable = false, unique = true, updatable = false)
//    private String uuid;
//
//
//
//    /**
//     * User's first name
//     */
//    @Column(name="first_name",nullable = true)
//    private String firstName;
//
//    /**
//     * User's last name
//     */
//    @Column(name="last_name",nullable = true)
//    private String lastName;
//
//
//
//
//    /**
//     * User's email address. Must be unique.
//     */
//    @Column(name="email",unique = true)
//    private String email;
//
//    /**
//     * User's phone number. Must be unique.
//     */
//    @Column(name="phone",unique = true)
//    private String phone;
//
//    /**
//     * Encrypted password for email-based login. Nullable for social/Otpless logins.
//     */
//    @Column(name="password",nullable = true)
//    private String password;
//
//
//    /**
//     * Role assigned to the user (e.g., USER, CREATOR, ADMIN).
//     */
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Role role;
//
//    /**
//     * * Channel through which the user logs in (e.g., EMAIL, GOOGLE, PHONE, WHATSAPP).
//     */
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private LoginChannel loginChannel;
//
//    /**
//     * Timestamp of when the user record was created.
//     */
//    @CreationTimestamp
//    private LocalDateTime createdAt;
//}
package com.auth.service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entity class that represents the credentials of a Fan user.
 * Includes identity, login details, and social links.
 */
@Entity
@Table(name = "fans")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Fan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique user identifier across services
     */
    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    private String uuid;

    /**
     * Full name of the fan
     */
    @Column(name = "full_name", nullable = false)
    private String fullName;

    /**
     * Username (must be unique)
     */
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    /**
     * User's email address
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * User's phone number
     */
    @Column(name = "phone", nullable = false, unique = true, length = 15)
    private String phone;

    /**
     * Encrypted password (nullable for social/otpless login)
     */
    @Column(name = "password", length = 100)
    private String password;

    /**
     * User biography
     */
    @Column(name = "bio", length = 500)
    private String bio;

    /**
     * Social media links (optional)
     */
    @Column(name = "instagram_link")
    private String instagramLink;

    @Column(name = "facebook_link")
    private String facebookLink;

    @Column(name = "youtube_link")
    private String youtubeLink;

    @Column(name = "x_url")
    private String xUrl;

    /**
     * Record creation timestamp
     */
    @CreationTimestamp
    private LocalDateTime createdAt;
}

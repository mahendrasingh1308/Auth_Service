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

    @Column(name = "role")
    private String role;

    /**
     * Record creation timestamp
     */
    @CreationTimestamp
    private LocalDateTime createdAt;
}

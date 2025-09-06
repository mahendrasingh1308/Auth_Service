package com.auth.service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "creators")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Creator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String uuid;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true, length = 15)
    private String mobile;

    private String gender;

    private LocalDate dateOfBirth;

    @Column(nullable = false, length = 100) // BCrypt hash ~60 chars
    private String password;

    @Column(length = 500)
    private String bio;

    private String company;
    private String country;
    private String state;
    private String city;
    private String postalCode;
    private String billingAddress;

    private String instagramUrl;
    private String facebookUrl;
    private String youtubeUrl;
    private String twitterUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "role")
    private String role;
}

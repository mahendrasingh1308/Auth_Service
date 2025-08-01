package com.auth.service.entity;

import com.auth.service.enums.Role;
import com.auth.service.enums.LoginChannel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_credentials")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String uuid; // Unique user ID for cross-service communication

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phone;

    @Column(nullable = true)
    private String password;

    @Column(nullable = true)
    private String firstName;

    @Column(nullable = true)
    private String lastName;

    @Column(unique = true)
    private String username; // ✅added for system- or user-generated usernames

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // USER, CREATOR, ADMIN

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoginChannel loginChannel; // EMAIL, GOOGLE, PHONE

    @CreationTimestamp
    private LocalDateTime createdAt;
}

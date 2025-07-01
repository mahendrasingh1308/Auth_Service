package com.auth.service.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity class that represents the user table in the database.

 * Stores user information like name, email, and password.
 * Used for authentication and authorization purposes.

 * Annotations from Lombok are used to reduce boilerplate code.
 *
 * @author Mahendra
 * @since 2025
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    /**
     * Primary key for the user table.
     * Auto-incremented ID.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the user.
     */
    private String name;

    /**
     * Unique email address of the user.
     * Used for login and identity.
     */
    @Column(unique = true)
    private String email;

    /**
     * Encrypted password of the user.
     */
    private String password;

    /**
     * Role of the user.
     */
    private String role;
}

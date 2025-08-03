package com.auth.service.config;

import com.auth.service.entity.UserCredential;
import com.auth.service.repository.UserCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Custom implementation of Spring Security's {@link UserDetailsService}.
 * <p>
 * Loads user-specific data from the database based on email.
 * Integrates with Spring Security for authentication and role-based authorization.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserCredentialRepository userCredentialRepository;

    /**
     * Loads a user by email (used as the username in the system).
     *
     * @param email the email address of the user
     * @return UserDetails object required by Spring Security
     * @throws UsernameNotFoundException if no user is found with the provided email
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserCredential user = userCredentialRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String safePassword = user.getPassword() != null ? user.getPassword() : "";

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                safePassword,
                getAuthorities(user)
        );
    }

    /**
     * Builds a list of authorities (roles) for Spring Security.
     *
     * @param user the user for whom to fetch authorities
     * @return list of granted authorities (e.g., ROLE_ADMIN, ROLE_USER)
     */
    public List<SimpleGrantedAuthority> getAuthorities(UserCredential user) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }
}

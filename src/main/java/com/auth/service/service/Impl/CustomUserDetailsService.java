package com.auth.service.service.Impl;

import com.auth.service.entity.Creator;
import com.auth.service.entity.Fan;
import com.auth.service.repository.CreatorRepository;
import com.auth.service.repository.FanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom UserDetailsService implementation that checks both
 * Creator and Fan repositories for authentication.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CreatorRepository creatorRepository;
    private final FanRepository fanRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. First check in Creator Repository
        Creator creator = creatorRepository.findByEmail(email).orElse(null);
        if (creator != null) {
            return User.builder()
                    .username(creator.getEmail())  // login with email
                    .password(creator.getPassword())
                    .roles("CREATOR")
                    .build();
        }

        // 2. If not found, check in Fan Repository
        Fan fan = fanRepository.findByEmail(email).orElse(null);
        if (fan != null) {
            return User.builder()
                    .username(fan.getEmail())  // login with email
                    .password(fan.getPassword())
                    .roles("FAN")
                    .build();
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}

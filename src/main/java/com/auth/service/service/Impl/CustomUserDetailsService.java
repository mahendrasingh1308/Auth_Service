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
 * Custom UserDetailsService implementation that prioritizes
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CreatorRepository creatorRepository;
    private final FanRepository fanRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {

        // First: Try by email
        Creator creatorByEmail = creatorRepository.findByEmail(identifier).orElse(null);
        if (creatorByEmail != null) {
            return User.builder()
                    .username(creatorByEmail.getEmail())
                    .password(creatorByEmail.getPassword())
                    .roles("CREATOR")
                    .build();
        }

        Fan fanByEmail = fanRepository.findByEmail(identifier).orElse(null);
        if (fanByEmail != null) {
            return User.builder()
                    .username(fanByEmail.getEmail())
                    .password(fanByEmail.getPassword())
                    .roles("FAN")
                    .build();
        }

        throw new UsernameNotFoundException("User not found with email: " + identifier);
    }
}

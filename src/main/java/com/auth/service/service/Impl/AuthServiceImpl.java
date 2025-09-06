package com.auth.service.service.Impl;

import com.auth.service.dto.*;
import com.auth.service.entity.Creator;
import com.auth.service.entity.Fan;
import com.auth.service.exception.CustomException;
import com.auth.service.jwt.JwtUtil;
import com.auth.service.repository.CreatorRepository;
import com.auth.service.repository.FanRepository;
import com.auth.service.service.AuthService;
import com.auth.service.util.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * Service implementation for handling authentication-related operations.
 * Supports registration and login for both Fans and Creators, as well as
 * JWT token management (access/refresh).
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final FanRepository fanRepository;
    private final CreatorRepository creatorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final Map<String, String> refreshTokenStore = new HashMap<>();
    private final Set<String> blacklistedTokens = new HashSet<>();

    @Override
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    @Override
    public FanSignupResponse registerFan(FanSignupRequest request) {
        if (fanRepository.findByEmail(request.getEmail()).isPresent()) {
            return new FanSignupResponse(AppConstants.EMAIL_ALREADY_EXISTS, null, null);
        }
        if (fanRepository.findByUsername(request.getUsername()).isPresent()) {
            return new FanSignupResponse(AppConstants.USERNAME_ALREADY_EXISTS, null, null);
        }
        if (fanRepository.findByPhone(request.getPhone()).isPresent()) {
            return new FanSignupResponse(AppConstants.PHONE_ALREADY_EXISTS, null, null);
        }

        String encodedPassword = request.getPassword() != null
                ? passwordEncoder.encode(request.getPassword())
                : null;

        Fan fan = Fan.builder()
                .uuid(UUID.randomUUID().toString())
                .fullName(request.getFullName())
                .username(request.getUsername())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(encodedPassword)
                .bio(request.getBio())
                .instagramLink(request.getInstagramLink())
                .facebookLink(request.getFacebookLink())
                .youtubeLink(request.getYoutubeLink())
                .xUrl(request.getXUrl())
                .build();

        fanRepository.save(fan);

        return new FanSignupResponse(AppConstants.SIGNUP_SUCCESS, fan.getUuid(), fan.getUsername());
    }

    @Override
    public CreatorSignupResponse registerCreator(CreatorSignupRequest request) {
        if (creatorRepository.findByEmail(request.getEmail()).isPresent()) {
            return new CreatorSignupResponse(null, null, null, AppConstants.EMAIL_ALREADY_EXISTS);
        }
        if (creatorRepository.findByUsername(request.getUsername()).isPresent()) {
            return new CreatorSignupResponse(null, null, null, AppConstants.USERNAME_ALREADY_EXISTS);
        }
        if (creatorRepository.findByMobile(request.getMobile()).isPresent()) {
            return new CreatorSignupResponse(null, null, null, AppConstants.MOBILE_ALREADY_EXISTS);
        }

        String encodedPassword = request.getPassword() != null
                ? passwordEncoder.encode(request.getPassword())
                : null;

        Creator creator = Creator.builder()
                .uuid(UUID.randomUUID().toString())
                .fullName(request.getFullName())
                .username(request.getUsername())
                .email(request.getEmail())
                .mobile(request.getMobile())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth() != null ? LocalDate.parse(request.getDateOfBirth()) : null)
                .password(encodedPassword)
                .bio(request.getBio())
                .company(request.getCompany())
                .country(request.getCountry())
                .state(request.getState())
                .city(request.getCity())
                .postalCode(request.getPostalCode())
                .billingAddress(request.getBillingAddress())
                .instagramUrl(request.getInstagramUrl())
                .facebookUrl(request.getFacebookUrl())
                .youtubeUrl(request.getYoutubeUrl())
                .twitterUrl(request.getTwitterUrl())
                .build();

        creatorRepository.save(creator);

        return new CreatorSignupResponse(creator.getUuid(), creator.getUsername(),
                creator.getFullName(), AppConstants.SIGNUP_SUCCESS);
    }

    @Override
    public JwtResponse login(LoginRequest request) {
        Fan fan = fanRepository.findByEmail(request.getEmail()).orElse(null);
        Creator creator = creatorRepository.findByEmail(request.getEmail()).orElse(null);

        if (fan == null && creator == null) {
            throw new CustomException(AppConstants.USER_NOT_FOUND);
        }

        String storedPassword;
        String uuid;
        String role;

        if (fan != null) {
            storedPassword = fan.getPassword();
            uuid = fan.getUuid();
            role = "FAN";
        } else {
            storedPassword = creator.getPassword();
            uuid = creator.getUuid();
            role = "CREATOR";
        }

        if (!passwordEncoder.matches(request.getPassword(), storedPassword)) {
            throw new CustomException(AppConstants.INVALID_CREDENTIALS);
        }

        String accessToken = jwtUtil.generateAccessToken(uuid, request.getEmail(), role);
        String refreshToken = jwtUtil.generateRefreshToken(uuid, request.getEmail(), role);

        refreshTokenStore.put(refreshToken, uuid);

        return new JwtResponse(accessToken, refreshToken, uuid, AppConstants.LOGIN_SUCCESS);
    }

    @Override
    public JwtResponse refreshToken(TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtUtil.validateToken(refreshToken) || !refreshTokenStore.containsKey(refreshToken)) {
            throw new CustomException(AppConstants.INVALID_REFRESH_TOKEN);
        }

        String uuid = jwtUtil.extractUuid(refreshToken);
        String email = jwtUtil.extractEmail(refreshToken);
        String role = jwtUtil.extractRole(refreshToken);

        String newAccessToken = jwtUtil.generateAccessToken(uuid, email, role);
        String newRefreshToken = jwtUtil.generateRefreshToken(uuid, email, role);

        refreshTokenStore.remove(refreshToken);
        refreshTokenStore.put(newRefreshToken, uuid);

        return new JwtResponse(newAccessToken, newRefreshToken, uuid, AppConstants.TOKEN_REFRESHED);
    }

    @Override
    public void logoutWithAccessToken(String accessToken) {
        if (accessToken != null && jwtUtil.validateToken(accessToken)) {
            blacklistedTokens.add(accessToken);

            String userEmail = jwtUtil.extractEmail(accessToken);
            refreshTokenStore.entrySet().removeIf(entry -> {
                try {
                    String tokenEmail = jwtUtil.extractEmail(entry.getKey());
                    return userEmail.equals(tokenEmail);
                } catch (Exception e) {
                    return false;
                }
            });
        }
    }

}

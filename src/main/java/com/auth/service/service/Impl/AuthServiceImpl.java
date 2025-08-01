package com.auth.service.service.Impl;

import com.auth.service.dto.*;
import com.auth.service.entity.UserCredential;
import com.auth.service.enums.LoginChannel;
import com.auth.service.enums.Role;
import com.auth.service.exception.CustomException;
import com.auth.service.jwt.JwtUtil;
import com.auth.service.repository.UserCredentialRepository;
import com.auth.service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.auth.service.util.AppConstants.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserCredentialRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(USER_ALREADY_EXISTS);
        }

        Role roleEnum;
        try {
            roleEnum = Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException("Invalid role: " + request.getRole());
        }

        LoginChannel loginChannel;
        try {
            loginChannel = LoginChannel.valueOf(request.getLoginChannel().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException("Invalid login channel: " + request.getLoginChannel());
        }

        // Generate unique username automatically
        String username = generateUniqueUsername(request.getFirstName(), request.getLastName());

        UserCredential user = UserCredential.builder()
                .uuid(UUID.randomUUID().toString())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roleEnum)
                .loginChannel(loginChannel)
                .username(username)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        userRepository.save(user);


        String message;
        switch (roleEnum) {
            case CREATOR -> message = CREATOR_REGISTERED_SUCCESSFULLY;
            case ADMIN -> message = ADMIN_REGISTERED_SUCCESSFULLY;
            default -> message = USER_REGISTERED_SUCCESSFULLY;
        }

        return new SignupResponse(message);
    }

    @Override
    public JwtResponse login(LoginRequest request) {
        UserCredential user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(INVALID_EMAIL_PASSWPRD));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(INVALID_EMAIL_PASSWPRD);
        }

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return new JwtResponse(accessToken, refreshToken, user.getUuid());
    }

    @Override
    public JwtResponse refreshToken(TokenRefreshRequest request) {
        String uuid = jwtUtil.extractUuid(request.getRefreshToken());

        if (!jwtUtil.isTokenExpired(request.getRefreshToken())) {
            UserCredential user = userRepository.findByUuid(uuid)
                    .orElseThrow(() -> new CustomException("User not found during refresh"));

            String newAccessToken = jwtUtil.generateAccessToken(user);
            return new JwtResponse(newAccessToken, request.getRefreshToken(), uuid);
        } else {
            throw new CustomException(REFERESH_TOKEN);
        }
    }

    @Override
    public UserCredential registerOrLoginWithOtpless(OtplessUser otplessUser) {
        if (otplessUser.getPhone() == null || otplessUser.getChannel() == null) {
            throw new CustomException("Invalid OTPLESS data: missing phone or channel.");
        }

        Optional<UserCredential> optional = userRepository.findByPhone(otplessUser.getPhone());
        UserCredential user;

        if (optional.isPresent()) {
            user = optional.get();
        } else {
            LoginChannel loginChannel;
            try {
                loginChannel = LoginChannel.valueOf(otplessUser.getChannel().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new CustomException("Invalid login channel: " + otplessUser.getChannel());
            }

            String baseName = otplessUser.getEmail() != null
                    ? otplessUser.getEmail().split("@")[0]
                    : "user";

            String username = generateUniqueUsername(baseName);

            user = UserCredential.builder()
                    .uuid(UUID.randomUUID().toString())
                    .email(otplessUser.getEmail())
                    .phone(otplessUser.getPhone())
                    .role(Role.USER)
                    .loginChannel(loginChannel)
                    .username(username)
                    .build();

            userRepository.save(user);
        }

        return user;
    }

    // Helper to generate username from first + last name
    private String generateUniqueUsername(String firstName, String lastName) {
        String base = (firstName + lastName).toLowerCase().replaceAll("\\s+", "");
        return generateUniqueUsername(base);
    }

    // Unique username from a base string
    private String generateUniqueUsername(String base) {
        String username;
        int attempt = 0;
        do {
            int randNum = 1000 + (int)(Math.random() * 9000);
            username = base + randNum;
            attempt++;
            if (attempt > 10_000) {
                throw new CustomException("Could not generate unique username");
            }
        } while (userRepository.existsByUsername(username));
        return username;
    }

    @Override
    public String logout(String token) {
        String uuid = jwtUtil.extractUuid(token);

        UserCredential user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new CustomException("User not found"));

        return switch (user.getRole()) {
            case CREATOR -> CREATOR_LOGGED_OUT_SUCCESSFULLY;
            case ADMIN -> ADMIN_LOGGED_OUT_SUCCESSFULLY;
            default -> USER_LOGGED_OUT_SUCCESSFULLY;
        };
    }
}

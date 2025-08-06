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

/**
 * Implementation of the AuthService interface.
 * <p>
 * Handles all core authentication logic including:
 * - User signup
 * - Login with email/password
 * - Refresh token generation
 * - OTP-less login (e.g., WhatsApp/SMS)
 * - Logout
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserCredentialRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user with the provided signup details.
     * Ensures email uniqueness, validates role and login channel,
     * and saves the user to the database.
     *
     * @param request the user signup request data
     * @return a response message indicating successful registration
     */
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

        UserCredential user = UserCredential.builder()
                .uuid(UUID.randomUUID().toString())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roleEnum)
                .loginChannel(loginChannel)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        userRepository.save(user);

        String message = switch (roleEnum) {
            case CREATOR -> CREATOR_REGISTERED_SUCCESSFULLY;
            case ADMIN -> ADMIN_REGISTERED_SUCCESSFULLY;
            default -> USER_REGISTERED_SUCCESSFULLY;
        };

        return new SignupResponse(message);
    }

    /**
     * Authenticates a user using email and password.
     * On success, returns a pair of JWT access and refresh tokens.
     *
     * @param request the login request containing email and password
     * @return JWT tokens and user UUID
     */
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

    /**
     * Refreshes the access token using a valid, non-expired refresh token.
     *
     * @param request contains the refresh token
     * @return new access token along with the original refresh token and user UUID
     */
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

    /**
     * Registers or logs in a user using OTP-less authentication methods
     * such as WhatsApp or SMS. If the user does not exist, a new user is created.
     *
     * @param otplessUser the user information from the OTP-less provider
     * @return the authenticated or newly registered user
     */
    @Override
    public UserCredential registerOrLoginWithOtpless(OtplessUser otplessUser) {
        if (otplessUser.getPhone() == null || otplessUser.getChannel() == null) {
            throw new CustomException("Invalid OTPLESS data: missing phone or channel.");
        }

        Optional<UserCredential> optional = userRepository.findByPhone(otplessUser.getPhone());

        if (optional.isPresent()) {
            return optional.get();
        }

        LoginChannel loginChannel;
        try {
            loginChannel = LoginChannel.valueOf(otplessUser.getChannel().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException("Invalid login channel: " + otplessUser.getChannel());
        }

        UserCredential user = UserCredential.builder()
                .uuid(UUID.randomUUID().toString())
                .email(otplessUser.getEmail())
                .phone(otplessUser.getPhone())
                .role(Role.USER)
                .loginChannel(loginChannel)
                .build();

        userRepository.save(user);
        return user;
    }

    /**
     * Logs out a user by decoding their token and returning
     * a role-specific logout message.
     *
     * @param token the JWT token to extract the user identity from
     * @return a logout confirmation message
     */
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

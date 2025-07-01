package com.auth.service.service;

import com.auth.service.dto.*;
import com.auth.service.entity.User;
import com.auth.service.exception.CustomException;
import com.auth.service.jwt.JwtUtil;
import com.auth.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.auth.service.util.AppConstants.*;

/**
 * Service class to handle authentication-related business logic including
 * user signup, login, and refreshing JWT access tokens.
 * This class works closely with UserRepository, JwtUtil, and PasswordEncoder.
 *
 * @author Mahendra
 * @since 2025
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    /**
     * Registers a new user after checking if the email is already taken.
     *
     * @param request SignupRequest containing user name, email, and password
     * @return SignupResponse indicating successful registration
     * @throws CustomException if user already exists
     */
    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(USER_ALREADY_EXISTS);
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        return new SignupResponse(USER_REGISTERED_SUCCESSFULLY);
    }

    /**
     * Authenticates a user by verifying credentials and issues JWT tokens.
     *
     * @param request LoginRequest containing email and password
     * @return JwtResponse containing access and refresh tokens
     * @throws CustomException if credentials are invalid
     */
    public JwtResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(INVALID_EMAIL_PASSWPRD));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(INVALID_EMAIL_PASSWPRD);
        }

        String accessToken = jwtUtil.generateAccessToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        return new JwtResponse(accessToken, refreshToken, user.getEmail());
    }

    /**
     * Validates the refresh token and generates a new access token.
     *
     * @param request TokenRefreshRequest containing refresh token
     * @return JwtResponse with new access token and existing refresh token
     * @throws CustomException if refresh token is expired
     */
    public JwtResponse refreshToken(TokenRefreshRequest request) {
        String username = jwtUtil.extractUsername(request.getRefreshToken());

        if (!jwtUtil.isTokenExpired(request.getRefreshToken())) {
            String newAccessToken = jwtUtil.generateAccessToken(username);
            return new JwtResponse(newAccessToken, request.getRefreshToken(), username);
        } else {
            throw new CustomException(REFERESH_TOKEN);
        }
    }
}

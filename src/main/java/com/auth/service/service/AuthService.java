package com.auth.service.service;

import com.auth.service.dto.*;
import com.auth.service.entity.UserCredential;

public interface AuthService {
    SignupResponse signup(SignupRequest request);
    JwtResponse login(LoginRequest request);
    JwtResponse refreshToken(TokenRefreshRequest request);
    UserCredential registerOrLoginWithOtpless(OtplessUser otplessUser);
    String logout(String token);
}

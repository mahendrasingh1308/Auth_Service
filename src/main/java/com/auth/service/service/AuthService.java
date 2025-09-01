package com.auth.service.service;
import com.auth.service.dto.*;

public interface AuthService {
    FanSignupResponse registerFan(FanSignupRequest request);
    CreatorSignupResponse registerCreator(CreatorSignupRequest request);

    JwtResponse login(LoginRequest request);
    JwtResponse refreshToken(TokenRefreshRequest request);
    void logout(String refreshToken);

}

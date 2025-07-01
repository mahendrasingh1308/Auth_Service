package com.auth.service.config;

import com.auth.service.entity.User;
import com.auth.service.repository.UserRepository;
import com.auth.service.jwt.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;

        if (userOptional.isEmpty()) {
            user = User.builder()
                    .email(email)
                    .name(name)
                    .role("ROLE_USER")
                    .password("") // empty password, not used for OAuth
                    .build();
            userRepository.save(user);
        } else {
            user = userOptional.get();
        }

        String token = jwtUtil.generateAccessToken(user.getEmail());

        response.sendRedirect("http://localhost:8081/swagger-ui/index.html?token=" + token);
    }
}

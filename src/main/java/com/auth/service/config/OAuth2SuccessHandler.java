package com.auth.service.config;

import com.auth.service.entity.UserCredential;
import com.auth.service.enums.LoginChannel;
import com.auth.service.enums.Role;
import com.auth.service.jwt.JwtUtil;
import com.auth.service.repository.UserCredentialRepository;
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
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserCredentialRepository userCredentialRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Optional<UserCredential> optionalUser = userCredentialRepository.findByEmail(email);
        UserCredential user;

        if (optionalUser.isEmpty()) {
            user = UserCredential.builder()
                    .uuid(UUID.randomUUID().toString()) // ✅ UUID required for token subject
                    .email(email)
                    .role(Role.USER)
                    .loginChannel(LoginChannel.GOOGLE) // or whatever provider you're using
                    .password(null) // Google login so no password
                    .build();
            userCredentialRepository.save(user);
        } else {
            user = optionalUser.get();
        }

        // ✅ Use UserCredential for token generation
        String token = jwtUtil.generateAccessToken(user);

        // ✅ Redirect with token (can also use cookie)
        response.sendRedirect("http://localhost:8081/swagger-ui/index.html?token=" + token);
    }
}

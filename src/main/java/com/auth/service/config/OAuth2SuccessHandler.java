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

/**
 * Handles successful OAuth2 authentication.
 * <p>
 * This handler:
 * - Extracts user info from OAuth2 provider (e.g., Google)
 * - Registers new users if they don't exist
 * - Generates JWT access token
 * - Redirects user with token as URL parameter
 * </p>
 */
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserCredentialRepository userCredentialRepository;
    private final JwtUtil jwtUtil;

    /**
     * Processes the successful OAuth2 login.
     * If the user does not exist, registers them with default role USER.
     * Generates an access token and redirects with it in the URL.
     *
     * @param request        the HTTP request
     * @param response       the HTTP response
     * @param authentication the authenticated principal (OAuth2User)
     * @throws IOException      if redirect fails
     * @throws ServletException if servlet error occurs
     */
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
                    .uuid(UUID.randomUUID().toString())
                    .email(email)
                    .role(Role.USER)
                    .loginChannel(LoginChannel.GOOGLE)
                    .password(null)
                    .build();
            userCredentialRepository.save(user);
        } else {
            user = optionalUser.get();
        }

        String token = jwtUtil.generateAccessToken(user);
        response.sendRedirect("http://localhost:8081/swagger-ui/index.html?token=" + token);
    }
}

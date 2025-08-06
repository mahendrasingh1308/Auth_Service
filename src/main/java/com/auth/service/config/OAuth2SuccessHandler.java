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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/**
 * Handles successful OAuth2 login from Google and Facebook.
 * Registers user if not present, issues JWT, and redirects with token.
 */
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserCredentialRepository userCredentialRepository;
    private final JwtUtil jwtUtil;

    @Value("${app.frontend.redirect-url}")
    private String frontendRedirectUrl;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");


        if (email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email not provided by OAuth2 provider.");
            return;
        }

        LoginChannel loginChannel = extractLoginChannelFromRequest(request);

        Optional<UserCredential> optionalUser = userCredentialRepository.findByEmail(email);
        UserCredential user;

        if (optionalUser.isEmpty()) {
            user = UserCredential.builder()
                    .uuid(UUID.randomUUID().toString())
                    .email(email)
                    .role(Role.USER)
                    .loginChannel(loginChannel)
                    .password(null)
                    .build();

            userCredentialRepository.save(user);
        } else {
            user = optionalUser.get();
        }

        String token = jwtUtil.generateAccessToken(user);

        // TODO: Use a configurable frontend URL (instead of hardcoded Swagger UI)
           response.sendRedirect(frontendRedirectUrl + "?token=" + token);

    }

    /**
     * Determine login channel (GOOGLE / FACEBOOK) from request URI
     */
    private LoginChannel extractLoginChannelFromRequest(HttpServletRequest request) {
        String uri = request.getRequestURI().toLowerCase();
        if (uri.contains("facebook")) return LoginChannel.FACEBOOK;
        if (uri.contains("google")) return LoginChannel.GOOGLE;
        return LoginChannel.EMAIL;
    }

}

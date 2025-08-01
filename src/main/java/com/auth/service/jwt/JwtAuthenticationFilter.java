package com.auth.service.jwt;

import com.auth.service.config.CustomUserDetailsService;
import com.auth.service.entity.UserCredential;
import com.auth.service.exception.InvalidTokenException;
import com.auth.service.repository.UserCredentialRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UserCredentialRepository userCredentialRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/api/auth/otpless/callback") ||
                path.equals("/api/auth/signup") ||
                path.equals("/api/auth/login") ||
                path.equals("/api/auth/refresh") ||
                path.equals("/api/auth/logout") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/swagger-resources");
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String uuid;

        try {
            uuid = jwtUtil.extractUuid(token);
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid or malformed JWT token");
        }

        if (uuid == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        UserCredential user = userCredentialRepository.findByUuid(uuid)
                .orElseThrow(() -> new InvalidTokenException("User not found for UUID: " + uuid));

        if (!jwtUtil.isTokenValid(token, uuid)) {
            throw new InvalidTokenException("Token is invalid or expired");
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user, null, userDetailsService.getAuthorities(user));
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}

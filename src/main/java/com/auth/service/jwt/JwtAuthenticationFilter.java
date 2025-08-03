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

/**
 * JWT Authentication Filter to validate and authorize requests based on JWT tokens.
 * This filter intercepts each request once and checks the token, then authenticates the user if valid.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UserCredentialRepository userCredentialRepository;

    /**
     * Defines URLs that should bypass JWT filter (public endpoints).
     *
     * @param request incoming HTTP request
     * @return true if the filter should be skipped for this request
     */
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

    /**
     * Validates JWT from Authorization header and sets authentication in the security context.
     *
     * @param request     incoming HTTP request
     * @param response    HTTP response
     * @param filterChain chain of filters to continue request processing
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs during filtering
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Proceed if Authorization header is missing or doesn't start with "Bearer "
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

        // Skip authentication if UUID is null or context already has authentication
        if (uuid == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Retrieve user from database
        UserCredential user = userCredentialRepository.findByUuid(uuid)
                .orElseThrow(() -> new InvalidTokenException("User not found for UUID: " + uuid));

        // Validate token against user UUID
        if (!jwtUtil.isTokenValid(token, uuid)) {
            throw new InvalidTokenException("Token is invalid or expired");
        }

        // Set authentication in context
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user, null, userDetailsService.getAuthorities(user));
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}

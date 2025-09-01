package com.auth.service.jwt;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utility class for generating, parsing, and validating JWT tokens.
 * <p>
 * This class supports:
 * <ul>
 *     <li>Access token generation</li>
 *     <li>Refresh token generation</li>
 *     <li>Token validation</li>
 *     <li>Extracting claims like UUID, email, and role</li>
 * </ul>
 *
 * <p><b>Configuration:</b><br>
 * Values are injected from {@code application.properties}:
 * <ul>
 *     <li>{@code security.jwt.secret} → Signing secret</li>
 *     <li>{@code security.jwt.access-token-expiration} → Access token validity (ms)</li>
 *     <li>{@code security.jwt.refresh-token-expiration} → Refresh token validity (ms)</li>
 * </ul>
 */
@Component
public class JwtUtil {

    /** JWT signing secret key (Base64 encoded) */
    @Value("${security.jwt.secret}")
    private String secret;

    /** Access token expiration time in milliseconds */
    @Value("${security.jwt.access-token-expiration}")
    private long accessTokenValidity;

    /** Refresh token expiration time in milliseconds */
    @Value("${security.jwt.refresh-token-expiration}")
    private long refreshTokenValidity;

    /**
     * Returns the signing key derived from the secret.
     *
     * @return HMAC-SHA256 signing key
     */
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Generates a JWT access token containing UUID, email, and role.
     *
     * @param uuid  Unique user identifier
     * @param email User email (used as subject)
     * @param role  User role
     * @return Signed JWT access token
     */
    public String generateAccessToken(String uuid, String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("uuid", uuid)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Generates a JWT refresh token containing UUID, email, and role.
     *
     * @param uuid  Unique user identifier
     * @param email User email (used as subject)
     * @param role  User role
     * @return Signed JWT refresh token
     */
    public String generateRefreshToken(String uuid, String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("uuid", uuid)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates a JWT token.
     *
     * @param token JWT token string
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Extracts email (subject) from the token.
     *
     * @param token JWT token string
     * @return email (subject claim)
     */
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Extracts UUID from the token.
     *
     * @param token JWT token string
     * @return UUID claim
     */
    public String extractUuid(String token) {
        return getClaims(token).get("uuid", String.class);
    }

    /**
     * Extracts role from the token.
     *
     * @param token JWT token string
     * @return role claim
     */
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    /**
     * Parses the claims inside the token.
     *
     * @param token JWT token string
     * @return Claims object
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

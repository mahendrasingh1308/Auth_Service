package com.auth.service.jwt;

import com.auth.service.entity.UserCredential;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utility class for handling JWT operations such as token creation, validation, and claims extraction.
 */
@Component
public class JwtUtil {

    /**
     * Secret key used to sign the JWT tokens.
     * This should be stored securely in production environments (e.g., in environment variables or secrets manager).
     */
    private final String SECRET = "4k3oZ+mUUGSS+c3BZ6knVEvrAa2eo/0Cd6Iu2DUF8Jo=";

    /**
     * Expiration time for access tokens: 20 minutes (in milliseconds).
     */
    private final long ACCESS_TOKEN_EXPIRATION = 20 * 60 * 1000;

    /**
     * Expiration time for refresh tokens: 7 days (in milliseconds).
     */
    private final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000;

    /**
     * Generates the HMAC signing key from the secret string.
     *
     * @return Key used for signing and verifying JWTs
     */
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /**
     * Generates a JWT access token for the given user. Subject = user's email.
     *
     * @param user the user for whom the token is being generated
     * @return the generated JWT access token
     */
    public String generateAccessToken(UserCredential user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("uuid", user.getUuid())
                .claim("role", user.getRole().name())
                .claim("loginChannel", user.getLoginChannel().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Generates a JWT refresh token for the given user. Subject = user's email.
     *
     * @param user the user for whom the refresh token is being generated
     * @return the generated JWT refresh token
     */
    public String generateRefreshToken(UserCredential user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the email (subject) from the given JWT token.
     *
     * @param token the JWT token
     * @return the email from the token
     */
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Extracts the UUID claim from the given JWT token.
     *
     * @param token the JWT token
     * @return the UUID claim from the token
     */
    public String extractUuid(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("uuid", String.class);
    }

    /**
     * Extracts the role claim from the given JWT token.
     *
     * @param token the JWT token
     * @return the role claim as a string
     */
    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    /**
     * Extracts the loginChannel claim from the given JWT token.
     *
     * @param token the JWT token
     * @return the loginChannel claim as a string
     */
    public String extractLoginChannel(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("loginChannel", String.class);
    }

    /**
     * Checks whether the JWT token is expired or not.
     *
     * @param token the JWT token
     * @return true if the token has expired, false otherwise
     */
    public boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    /**
     * Validates whether the given token is valid for the expected email (subject).
     *
     * @param token         the JWT token
     * @param expectedEmail the expected email (subject) of the token
     * @return true if the token is valid and not expired, false otherwise
     */
    public boolean isTokenValid(String token, String expectedEmail) {
        return extractEmail(token).equals(expectedEmail) && !isTokenExpired(token);
    }
}

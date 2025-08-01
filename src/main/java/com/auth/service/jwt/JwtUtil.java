package com.auth.service.jwt;

import com.auth.service.entity.UserCredential;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET = "4k3oZ+mUUGSS+c3BZ6knVEvrAa2eo/0Cd6Iu2DUF8Jo=";
    private final long ACCESS_TOKEN_EXPIRATION = 20 * 60 * 1000;       // 20 minutes
    private final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7 days

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    //  Access Token - Subject = email
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

    //  Refresh Token - Subject = email
    public String generateRefreshToken(UserCredential user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //  Extract email from subject
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    //  Extract uuid from claims
    public String extractUuid(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("uuid", String.class);
    }

    //  Extract role
    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    //  Extract loginChannel
    public String extractLoginChannel(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("loginChannel", String.class);
    }

    // Token expiry check
    public boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    // Token valid check
    public boolean isTokenValid(String token, String expectedEmail) {
        return extractEmail(token).equals(expectedEmail) && !isTokenExpired(token);
    }
}

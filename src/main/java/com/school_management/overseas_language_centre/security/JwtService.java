package com.school_management.overseas_language_centre.security;


import com.school_management.overseas_language_centre.property.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties jwtProperty;


    /** Build a signed access token with {@code sub=username} and configured TTL. */
    public String generateToken(String username) {
        // Derive HMAC key from configured secret (must be long enough for HS256)
        SecretKey key = Keys.hmacShaKeyFor(jwtProperty.getSecret().getBytes(StandardCharsets.UTF_8));
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperty.getExpirationMs());

        return Jwts.builder()
                .subject(username)
                .claim("type", "access")// becomes the authenticated principal name
                .issuedAt(now)       // iat claim
                .expiration(expiry)  // exp claim — filter rejects tokens past this instant
                .signWith(key)       // HMAC-SHA256 signature
                .compact();
    }
    public String generateRefreshToken(String username) {

        SecretKey key = Keys.hmacShaKeyFor(
                jwtProperty.getSecret().getBytes(StandardCharsets.UTF_8)
        );

        Date now = new Date();

        Date expiry = new Date(
                now.getTime() + jwtProperty.getRefreshExpirationMs()
        );

        return Jwts.builder()
                .subject(username)
                .claim("type", "refresh")
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    /** Parse and verify the token, then return the subject (username). Caller must validate first. */
    public String getUsernameFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtProperty.getSecret().getBytes(StandardCharsets.UTF_8));
        Claims payload = Jwts.parser()
                .verifyWith(key)     // verify signature before reading claims
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return payload.getSubject();
    }

    /**
     * Return {@code true} only when signature is valid and token is not expired.
     * Any parse/verify failure is swallowed — filter treats it as "no auth".
     */
    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtProperty.getSecret().getBytes(StandardCharsets.UTF_8));
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            //log.debug("Invalid JWT: {}", e.getMessage());
            return false;
        }
    }

    //use for validate on refresh token
    public boolean validateRefreshToken(String token) {

        try {

            SecretKey key = Keys.hmacShaKeyFor(
                    jwtProperty.getSecret().getBytes(StandardCharsets.UTF_8)
            );

            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return "refresh".equals(claims.get("type", String.class));

        } catch (Exception e) {

            return false;
        }
    }

    public Duration getExpirationDuration() {
        return Duration.ofMillis(jwtProperty.getExpirationMs());
    }

    public Duration getRefreshExpirationDuration() {
        return Duration.ofMillis(
                jwtProperty.getRefreshExpirationMs()
        );
    }
}
package com.project.notebook.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JwtUtil — generates and validates JWT tokens.
 *
 * Token payload contains:  subject = userId (as String)
 * Token is valid for 7 days.
 *
 * IMPORTANT: Move SECRET_KEY to application.properties in production
 *   and inject it with @Value("${jwt.secret}").
 */
@Component
public class JwtUtil {

    // Must be ≥ 256 bits (32 chars) for HS256
    private static final String SECRET = "nota-super-secret-key-change-me-in-prod-!!!";
    private static final long   EXPIRY  = 7L * 24 * 60 * 60 * 1000; // 7 days in ms

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    /** Create a signed token embedding the user's id. */
    public String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRY))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** Extract userId from a valid token. Returns null on any error. */
    public Long extractUserId(String token) {
        try {
            String subject = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return Long.parseLong(subject);
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    /** Returns true if the token is structurally valid and not expired. */
    public boolean isValid(String token) {
        return extractUserId(token) != null;
    }
}

package com.mediconnect.appointment_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    public Key getSiginingKey() {

        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String extractEmail(String token) {

        return Jwts
                .parserBuilder()
                .setSigningKey(getSiginingKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            extractEmail(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long extractUserId(String token) {

        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSiginingKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userId", Long.class);
    }

    public String extractRole(String token) {

        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSiginingKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }


}

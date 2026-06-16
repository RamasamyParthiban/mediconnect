package com.mediconnect.user_service.security;

import com.mediconnect.user_service.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret; // read from config server

    @Value("${jwt.expiration}")
    private Long expiration; // read from config server (86400000 = 24hrs)

    public Key getSigningKey() {    // Converts secret string to cryptographic Key

        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String email, Role role, Long userID) { // Creates JWT token after successful login

        Map<String, Object> claim = new HashMap<>();

        claim.put("role", role);
        claim.put("userId", userID);

        return Jwts.builder()
                .setClaims(claim)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    // Reads email from token
    public String extractEmail(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Checks if token is valid and not expired
    public boolean validateToken(String token) {

        try {
            extractEmail(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
            // ExpiredJwtException    → token expired
            // MalformedJwtException  → token corrupted
            // SignatureException     → token tampered
        }
    }

    public Long extractUserID(String token) {

        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userId", Long.class);
    }

    public String extractRole(String token){

        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

       return  claims.get("role", String.class);
    }
}

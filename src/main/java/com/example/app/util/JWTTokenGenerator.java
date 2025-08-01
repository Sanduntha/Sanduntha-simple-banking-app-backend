package com.example.app.util;

import com.example.app.entity.User;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JWTTokenGenerator {

    // Key generation for JWT signing
    SecretKey key = Jwts.SIG.HS256.key().build();

    // JWT Token generation with claims
    public String generateToken(User user) {
        long EXPIRATION_TIME = 1000 * 60 * 60;

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }


    public boolean verifyToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (JwtException e) {
            System.out.println("Invalid JWT: " + e.getMessage());
            return false;
        }
    }


    public String encodePassword(String rawPassword) {
        return Base64.getEncoder().encodeToString(rawPassword.getBytes());
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return encodePassword(rawPassword).equals(encodedPassword);
    }

    public Jws<Claims> getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }

}

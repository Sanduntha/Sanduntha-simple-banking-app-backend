//package com.example.app;
//
//import com.example.app.entity.User;
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import javax.crypto.SecretKey;
//import java.util.Base64;
//import java.util.Date;
//
//public class JWTTokenGenerator {
//
//    SecretKey key = Jwts.SIG.HS256.key().build();
//
//    public String generateToken(User user) {
//        long EXPIRATION_TIME = 1000 * 60 * 60;
//
//        return Jwts.builder()
//                .setSubject(user.getEmail())
//                .claim("id", user.getId())
//                .claim("role", user.getRole())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(key)
//                .compact();
//    }
//
//    public boolean verifyToken(String token) {
//        try {
//            Jws<Claims> claims = Jwts.parser()
//                    .verifyWith(key)
//                    .build()
//                    .parseSignedClaims(token);
//
//            return true;
//        } catch (JwtException e) {
//            System.out.println("Invalid JWT: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public String encodePassword(String password) {
//        return Base64.getEncoder().encodeToString(password.getBytes());
//    }
//
//    public boolean matches(String rawPassword, String encodedPassword) {
//        String encodedRaw = encodePassword(rawPassword); // Encode again
//        return encodedRaw.equals(encodedPassword); // Compare both encoded
//    }
//
//}

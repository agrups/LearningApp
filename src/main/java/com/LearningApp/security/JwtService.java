package com.LearningApp.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.nio.charset.StandardCharsets;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expirationMs:86400000}")
    private long expirationMs;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {
        Date currentDate = new Date();

        Date expireDate = new Date(currentDate.getTime() + expirationMs);

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expireDate)
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validateToken(String token) {

        Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parse(token);
        return true;
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}

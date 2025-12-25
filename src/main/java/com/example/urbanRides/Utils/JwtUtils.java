package com.example.urbanRides.Utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    // #1- we're taking random str as secret key. (.env)
    @Value("${jwt.secret}")       // <-- read from YAML
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private Long expire;

    // #2- we're using cryptography to make secrete key stronger and readable
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // #3- main we're generating JWT token
    public String generateToken( String username) {
        return Jwts.builder()
                .setSubject(username) //username or userid will set as subject.
                .setIssuedAt(new Date()) // token creation date
                .setExpiration(new Date(System.currentTimeMillis() + expire)) // token exp date/time (24 hour)
                .signWith(getSignKey(), SignatureAlgorithm.HS256) // just creating signature.
                .compact(); // wrap up everything and convert into JWT token
    }

    // when user hits protected api all single process

    // #5- token validation
    public boolean validateToken(String token, String username) {
        String tokenUsername = extractUsername(token);
        return tokenUsername.equals(username) && !isTokenExpired(token);
    }

    // #6- part of validateToken - get username from extracted claims
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // #7- part of validateToken - checks token is expired
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // it happens simultaneously while extractUsername & isTokenExpired
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}

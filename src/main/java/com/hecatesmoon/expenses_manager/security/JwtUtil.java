package com.hecatesmoon.expenses_manager.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    private SecretKey key;

    @PostConstruct
    public void init(){
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Long id){

        Date now = new Date();

        return Jwts.builder()
                   .subject(String.valueOf(id)) //todo: consider use email
                   .issuedAt(now)
                   .expiration(new Date(now.getTime() + jwtExpirationMs))
                   .signWith(key)
                   .compact();
    }

    public Long getIdFromToken(String token){
        String idFromToken = Jwts.parser().verifyWith(key).build()
                                .parseSignedClaims(token)
                                .getPayload()
                                .getSubject();

        return Long.valueOf(idFromToken);
    }

    public boolean validateJwtToken(String token){
        try{
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e){
            System.err.println("JWT validation error: " + e.getMessage()); //todo: log
        }
        return false;
    }
}

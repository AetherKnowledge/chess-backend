package com.aethernet.chessbackend.auth.jwt;

import com.aethernet.chessbackend.user.dto.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

public class Jwt {

    private final SecretKey key;
    private final Claims claims;

    public Jwt(Claims claims, SecretKey key) {
        this.key = key;
        this.claims = claims;
    }

    public boolean isExpired() {
        return !claims.getExpiration().after(new Date());
    }

    public UUID getUserId(){
        return UUID.fromString(claims.getSubject());
    }

    public String getEmail(){
        return String.valueOf(claims.get("email"));
    }

    public String getName(){
        return String.valueOf(claims.get("name"));
    }

    public Role getRole(){
        return Role.valueOf(claims.get("role", String.class));
    }

    public String toString(){
        return Jwts.builder()
                .claims(claims)
                .signWith(key)
                .compact();
    }
}
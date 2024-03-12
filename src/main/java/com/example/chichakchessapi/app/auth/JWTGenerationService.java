package com.example.chichakchessapi.app.auth;

import com.example.chichakchessapi.app.BaseService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import static com.example.chichakchessapi.app.auth.AuthService.TOKEN_VALIDITY_SECS;

@Service
public class JWTGenerationService extends BaseService {
    private final Key jwtKey;

    public JWTGenerationService(@Value("${app.jwt.secret}") String jwtSecret) {
        this.jwtKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateJWTToken(Map<String, Object> claims, UserDetails userDetails) {
        Date issuedAt = new Date();
        Date expiration = Date.from(issuedAt.toInstant().plusSeconds(TOKEN_VALIDITY_SECS));

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .addClaims(claims)
                .signWith(jwtKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean verifyIfJWTTokenIsValid(String token, UserDetails userDetails) {
        String playerEmail = extractClaims(token).getSubject();
        return playerEmail.equals(userDetails.getUsername()) && !verifyIfTokenIsExpired(token);
    }

    public boolean verifyIfTokenIsExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}

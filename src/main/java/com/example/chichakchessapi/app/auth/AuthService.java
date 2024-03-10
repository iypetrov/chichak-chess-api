package com.example.chichakchessapi.app.auth;

import com.example.chichakchessapi.app.BaseService;
import com.example.chichakchessapi.app.auth.dtos.LoginRequestDTO;
import com.example.chichakchessapi.app.auth.dtos.RegisterRequestDTO;
import com.example.chichakchessapi.app.auth.models.LoginModel;
import com.example.chichakchessapi.app.auth.models.RegisterModel;
import com.example.chichakchessapi.app.common.CustomMessageUtil;
import com.example.chichakchessapi.app.players.PlayerService;
import com.example.chichakchessapi.app.players.models.PlayerModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.ResponseCookie;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService extends BaseService {
    public static final long TOKEN_VALIDITY_SECS = 14400L;
    public static final String COOKIE_AUTH_TOKEN_NAME = "AUTH-TOKEN";
    public static final String ROLE_CLAIM_NAME = "role";

    private final Key jwtKey;

    private final PlayerService playerService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            @Value("${app.jwt.secret}") String jwtSecret,
            PlayerService playerService,
            PasswordEncoder passwordEncoder
    ) {
        this.jwtKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.playerService = playerService;
        this.passwordEncoder = passwordEncoder;
    }

    public PlayerModel register(RegisterModel registerModel) {
        PlayerModel playerModel = playerService.createPlayer(registerModel);
        Map<String, Object> claims = new HashMap<>();
        claims.put(ROLE_CLAIM_NAME, playerModel.getRole());

        playerModel.setJwtToken(
                generateJWTToken(
                        claims,
                        playerModel
                )
        );

        return playerModel;
    }

    public PlayerModel login(LoginModel loginModel) {
        PlayerModel playerModel = playerService.getPlayerByEmail(loginModel.getEmail());
        String encodedPassword = playerService.getPlayersEncodedPasswordByID(playerModel.getId());
        if (!passwordEncoder.matches(loginModel.getPassword(), encodedPassword)) {
            throw unauthorized(
                    CustomMessageUtil.PLAYER_WRONG_PASSWORD,
                    CustomMessageUtil.PLAYER_ID + playerModel.getId()
            ).get();
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put(ROLE_CLAIM_NAME, playerModel.getRole());

        playerModel.setJwtToken(
                generateJWTToken(
                        claims,
                        playerModel
                )
        );

        return playerModel;
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

    public ResponseCookie getCookie(String name, String value, long maxAgeSeconds) {
        return ResponseCookie.from(name, value)
                .maxAge(maxAgeSeconds)
                .path("/")
                .secure(false)
                .httpOnly(false)
                .build();
    }
}

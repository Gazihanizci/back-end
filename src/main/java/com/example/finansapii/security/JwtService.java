package com.example.finansapii.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final SecretKey accessKey;
    private final SecretKey refreshKey;

    private final long accessExpMinutes;
    private final long refreshExpDays;

    public JwtService(
            @Value("${app.jwt.accessSecret}") String accessSecret,
            @Value("${app.jwt.refreshSecret}") String refreshSecret,
            @Value("${app.jwt.accessExpMinutes}") long accessExpMinutes,
            @Value("${app.jwt.refreshExpDays}") long refreshExpDays
    ) {
        this.accessKey = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
        this.refreshKey = Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));
        this.accessExpMinutes = accessExpMinutes;
        this.refreshExpDays = refreshExpDays;
    }

    // =========================
    // TOKEN GENERATION
    // =========================

    public String generateAccessToken(Long kullaniciId, String email) {
        Instant now = Instant.now();
        Instant exp = now.plus(accessExpMinutes, ChronoUnit.MINUTES);

        return Jwts.builder()
                .subject(String.valueOf(kullaniciId)) // sub = userId
                .claims(Map.of(
                        "email", email,
                        "typ", "access"
                ))
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(accessKey)
                .compact();
    }

    public String generateRefreshToken(Long kullaniciId) {
        Instant now = Instant.now();
        Instant exp = now.plus(refreshExpDays, ChronoUnit.DAYS);

        return Jwts.builder()
                .subject(String.valueOf(kullaniciId)) // sub = userId
                .claims(Map.of("typ", "refresh"))
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(refreshKey)
                .compact();
    }

    // =========================
    // CLAIMS PARSING
    // =========================

    public Claims parseAccessClaims(String token) {
        return Jwts.parser()
                .verifyWith(accessKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Claims parseRefreshClaims(String token) {
        return Jwts.parser()
                .verifyWith(refreshKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // =========================
    // EXTRACTORS
    // =========================

    public Long extractUserIdFromAccess(String token) {
        return Long.parseLong(parseAccessClaims(token).getSubject());
    }

    public Long extractUserIdFromRefresh(String token) {
        return Long.parseLong(parseRefreshClaims(token).getSubject());
    }

    public String extractTypeFromAccess(String token) {
        return parseAccessClaims(token).get("typ", String.class);
    }

    public String extractTypeFromRefresh(String token) {
        return parseRefreshClaims(token).get("typ", String.class);
    }

    // =========================
    // VALIDATORS
    // =========================

    public boolean isAccessValid(String token) {
        try {
            Claims c = parseAccessClaims(token);
            String typ = c.get("typ", String.class);
            return "access".equals(typ);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRefreshValid(String token) {
        try {
            Claims c = parseRefreshClaims(token);
            String typ = c.get("typ", String.class);
            return "refresh".equals(typ);
        } catch (Exception e) {
            return false;
        }
    }
}

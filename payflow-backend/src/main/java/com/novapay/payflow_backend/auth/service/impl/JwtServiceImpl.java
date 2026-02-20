package com.novapay.payflow_backend.auth.service.impl;

import com.novapay.payflow_backend.auth.service.JwtService;
import com.novapay.payflow_backend.common.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.access.expiration}")
    private Long EXPIRATION_TIME;
    @Value("${jwt.refresh.expiration}")
    private Long REFRESH_EXPIRATION_TIME;
    private static final String ACCESS_TOKEN_TYPE = "access_token";
    private static final String REFRESH_TOKEN_TYPE = "refresh_token";


    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private <T> T extractClaim(String token, java.util.function.Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    @Override
    public String generateToken(String username, Boolean isAccessToken) {
        Long expTime = isAccessToken ? EXPIRATION_TIME : REFRESH_EXPIRATION_TIME;
        String tokenType = isAccessToken ? ACCESS_TOKEN_TYPE : REFRESH_TOKEN_TYPE;
        Map<String, Object> claims = new HashMap<>();
        claims.put("typ", tokenType);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expTime))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public boolean validateToken(String token) {
        if (this.isTokenExpired(token)) {
            return false;
        }
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.ExpiredJwtException exception) {
            throw new UnauthorizedException("JWT token is expired. Please login again");
        } catch (JwtException | IllegalArgumentException exception) {
            throw new UnauthorizedException("Invalid JWT token");
        }
    }

    @Override
    public Boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

    @Override
    public Boolean isRefreshToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String tokenType = (String) claims.get("typ");
        return tokenType.equals(REFRESH_TOKEN_TYPE);
    }

    @Override
    public Boolean isAccessToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String tokenType = (String) claims.get("typ");
        return tokenType.equals(ACCESS_TOKEN_TYPE);
    }

    @Override
    public LocalDateTime getExpirationDate(String token) {
        Date date = extractClaim(token, Claims::getExpiration);
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}

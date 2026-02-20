package com.novapay.payflow_backend.auth.service;

import java.time.LocalDateTime;


public interface JwtService {
    String generateToken(String username, Boolean isAccessToken);

    String getUsernameFromToken(String token);

    boolean validateToken(String token);

    Boolean isTokenExpired(String token);

    Boolean isRefreshToken(String token);

    Boolean isAccessToken(String token);

    LocalDateTime getExpirationDate(String token);

}

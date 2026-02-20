package com.novapay.payflow_backend.auth.service;

import com.novapay.payflow_backend.auth.dto.request.LoginRequest;
import com.novapay.payflow_backend.auth.dto.request.RefreshTokenRequest;
import com.novapay.payflow_backend.auth.dto.request.RegisterRequest;
import com.novapay.payflow_backend.auth.dto.response.JwtResponse;
import com.novapay.payflow_backend.user.dto.response.UserResponse;

public interface AuthService {
    JwtResponse login(LoginRequest loginRequest);

    JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    JwtResponse register(RegisterRequest registerRequest);

    UserResponse getCurrentUser();

    void logout (String token);

}


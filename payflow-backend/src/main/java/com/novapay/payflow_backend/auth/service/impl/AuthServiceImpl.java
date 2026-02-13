package com.novapay.payflow_backend.auth.service.impl;

import com.novapay.payflow_backend.auth.dto.request.LoginRequest;
import com.novapay.payflow_backend.auth.dto.response.LoginResponse;
import com.novapay.payflow_backend.auth.security.JwtService;
import com.novapay.payflow_backend.auth.service.AuthService;
import com.novapay.payflow_backend.common.exception.ResourceNotFoundException;
import com.novapay.payflow_backend.user.entity.User;
import com.novapay.payflow_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public LoginResponse userLogin(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("Invalid email or password");
        }
        String token = jwtService.generateToken(loginRequest.getEmail());
        return LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .build();
    }
}

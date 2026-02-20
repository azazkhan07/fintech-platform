package com.novapay.payflow_backend.auth.service.impl;

import com.novapay.payflow_backend.auth.dto.request.LoginRequest;
import com.novapay.payflow_backend.auth.dto.request.RefreshTokenRequest;
import com.novapay.payflow_backend.auth.dto.request.RegisterRequest;
import com.novapay.payflow_backend.auth.dto.response.JwtResponse;
import com.novapay.payflow_backend.auth.mapper.AuthMapper;
import com.novapay.payflow_backend.auth.service.AuthService;
import com.novapay.payflow_backend.auth.service.JwtService;
import com.novapay.payflow_backend.auth.service.TokenBlackListService;
import com.novapay.payflow_backend.common.exception.ResourceNotFoundException;
import com.novapay.payflow_backend.common.exception.UnauthorizedException;
import com.novapay.payflow_backend.user.dto.request.UserRequest;
import com.novapay.payflow_backend.user.dto.response.UserResponse;
import com.novapay.payflow_backend.user.entity.User;
import com.novapay.payflow_backend.user.mapper.UserMapper;
import com.novapay.payflow_backend.user.repository.UserRepository;
import com.novapay.payflow_backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final AuthMapper authMapper;
    private final UserService userService;
    private final TokenBlackListService tokenBlackListService;


    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        LOGGER.info("Login attempt for email={}", loginRequest.getEmail());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()));
        } catch (Exception exception) {
            LOGGER.warn("Failed login attempt for email={}", loginRequest.getEmail());
            throw exception;
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        LOGGER.info("User authenticated successfully email={}", loginRequest.getEmail());

        String accessToken = jwtService.generateToken(user.getEmail(), true);
        String refreshToken = jwtService.generateToken(user.getEmail(),
                false);
        LOGGER.info("JWT tokens generated for email={}", user.getEmail());
        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userMapper.toResponseDTO(user))
                .build();
    }


    @Override
    public JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

        LOGGER.info("Refresh token request received");

        if (!jwtService.validateToken(refreshTokenRequest.getRefreshToken()) || !jwtService.isRefreshToken(refreshTokenRequest.getRefreshToken())) {
            LOGGER.warn("Invalid refresh token used");
            throw new UnauthorizedException("Invalid refresh token");
        }
        String usernameFromRefreshToken = jwtService.getUsernameFromToken(refreshTokenRequest.getRefreshToken());

        LOGGER.info("Refresh token valid");

        User user = userRepository.findByEmail(usernameFromRefreshToken)
                .orElseThrow(() -> new ResourceNotFoundException("User not found from refresh token"));
        UserResponse response = userMapper.toResponseDTO(user);
        String accessToken = jwtService.generateToken(response.email(), true);
        String newRefreshToken = jwtService.generateToken(user.getEmail(), false);

        LOGGER.info("New JWT token generated");

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .user(response)
                .build();
    }

    @Override
    public JwtResponse register(RegisterRequest registerRequest) {
        LOGGER.info("Register request for email={}", registerRequest.getEmail());
        UserRequest userRequest = authMapper.mapToUserRequest(registerRequest);
        UserResponse userResponse = userService.createUser(userRequest);

        String accessToken = jwtService.generateToken(userResponse.email(), true);
        String refreshToken = jwtService.generateToken(userResponse.email(), false);

        LOGGER.info("User registered and auto logged in {}", userResponse.email());

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userResponse)
                .build();
    }

    @Override
    public UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            throw new UnauthorizedException("User not authenticated");
        }
        String email = authentication.getName();
        LOGGER.info("Fetching current logged-in user {}", email);
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toResponseDTO(user);
    }

    @Override
    public void logout(String token) {
        LOGGER.info("Logout request received");
        tokenBlackListService.blacklistToken(token);
    }
}




package com.novapay.payflow_backend.auth.controller;

import com.novapay.payflow_backend.auth.dto.request.LoginRequest;
import com.novapay.payflow_backend.auth.dto.request.RefreshTokenRequest;
import com.novapay.payflow_backend.auth.dto.request.RegisterRequest;
import com.novapay.payflow_backend.auth.dto.response.JwtResponse;
import com.novapay.payflow_backend.auth.service.AuthService;
import com.novapay.payflow_backend.common.exception.UnauthorizedException;
import com.novapay.payflow_backend.user.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Jwt Security APIs")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @Operation(summary = "User login Api")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "User Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")})
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LOGGER.info("Received login request for email = {}", loginRequest.getEmail());
        JwtResponse response = authService.login(loginRequest);
        LOGGER.info("User login successful");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "generate new refresh token using refresh token")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Refresh token generate successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid refresh token")})
    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        LOGGER.info("Refresh token request received");
        JwtResponse response = authService.refreshToken(refreshTokenRequest);
        LOGGER.info("New access token generated successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "User Registration API")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "409", description = "User already exists")})
    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        JwtResponse response = authService.register(registerRequest);
        LOGGER.info("User SignIn Successfully with token");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get logged-in user profile")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "User fetched"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        UserResponse response = authService.getCurrentUser();
        LOGGER.info("Get logged-in user");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Validate user APIs")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "User validate"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @GetMapping("/validate")
    public ResponseEntity<?> validate(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "valid", true,
                "username", authentication.getName()));
    }

    @Operation(summary = "User logged out api")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "User logged out"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing or invalid Authorization header");
        }
        String token = header.substring(7);
        authService.logout(token);
        LOGGER.info("User logged out");
        return ResponseEntity.status(HttpStatus.OK).body("Logged out successfully");
    }
}

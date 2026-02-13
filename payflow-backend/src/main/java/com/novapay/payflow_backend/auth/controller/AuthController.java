package com.novapay.payflow_backend.auth.controller;

import com.novapay.payflow_backend.auth.dto.request.LoginRequest;
import com.novapay.payflow_backend.auth.dto.response.LoginResponse;
import com.novapay.payflow_backend.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "User login Api")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "User Login successful"),
            @ApiResponse(responseCode = "404", description = "Invalid user")})
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.userLogin(loginRequest));
    }
}

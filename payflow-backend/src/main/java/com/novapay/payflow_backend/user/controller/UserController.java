package com.novapay.payflow_backend.user.controller;

import com.novapay.payflow_backend.common.dto.ApiMessage;
import com.novapay.payflow_backend.wallet.dto.response.KycStatusResponse;
import com.novapay.payflow_backend.user.dto.request.KycRequest;
import com.novapay.payflow_backend.user.dto.request.UpdateUserRequest;
import com.novapay.payflow_backend.user.dto.request.UserRequest;
import com.novapay.payflow_backend.user.dto.response.UserResponse;
import com.novapay.payflow_backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User APIs", description = "Users Management Endpoints")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "User Created")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "User Created"),
    @ApiResponse(responseCode = "409", description = "User already exists")})
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        LOGGER.info("Create user request for email {} ", userRequest.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userRequest));
    }

    @Operation(summary = "Get User By Id")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "User Found"),
    @ApiResponse(responseCode = "404", description = "User Not Found")})
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
        UserResponse response = userService.getUserById(userId);
        LOGGER.info("User found with id {} ", userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "User update by id")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "User updated"),
    @ApiResponse(responseCode = "404", description = "User not found")})
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long userId, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        UserResponse response = userService.updateUser(userId, updateUserRequest);
        LOGGER.info("User updating with id {} ", userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "User Kyc Submission")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "User kyc submit"),
    @ApiResponse(responseCode = "404", description = "User not found")})
    @PostMapping("/{userId}/kyc")
    public ResponseEntity<ApiMessage> submitKyc(@PathVariable Long userId, @Valid @RequestBody KycRequest kycRequest) {
        userService.submitKyc(userId,kycRequest);
        LOGGER.info("User submitted with id {} ", userId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiMessage("KYC submitted successfully"));
    }

    @Operation(summary = "Getting Kyc Status")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Getting kyc status"),
    @ApiResponse(responseCode = "404", description = "Status not found")})
    @GetMapping("/{userId}/kyc-status")
    public ResponseEntity<KycStatusResponse> getKycStatus(@PathVariable Long userId){
        Boolean kycStatus = userService.getKycStatus(userId);
        LOGGER.info("Fetching KYC status for user{}",userId);
        return ResponseEntity.status(HttpStatus.OK).body(new KycStatusResponse(kycStatus));
    }

    @Operation(summary = "User Kyc Verifying")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "User kyc verify"),
    @ApiResponse(responseCode = "404", description = "User kyc not verified")})
    @PutMapping("/{userId}/verify-kyc")
    public ResponseEntity<ApiMessage> verifyKyc(@PathVariable Long userId) {
        userService.verifyKyc(userId);
        LOGGER.info("User kyc verified with id {} ", userId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiMessage("KYC verified successfully"));
    }
}

package com.novapay.payflow_backend.user.controller;

import com.novapay.payflow_backend.user.dto.request.UserRequest;
import com.novapay.payflow_backend.user.dto.response.UserResponse;
import com.novapay.payflow_backend.user.entity.User;
import com.novapay.payflow_backend.user.mapper.UserMapper;
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
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Operation(summary = "User Created")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "User Created"),
            @ApiResponse(responseCode = "409", description = "User already exists")})
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        User user = userMapper.toUserEntity(userRequest);
        User savedUser = userService.createUser(user);
        LOGGER.info("User created with id {} ", savedUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toResponseDTO(savedUser));
    }

    @Operation(summary = "Get User By Id")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "User Found"),
            @ApiResponse(responseCode = "404", description = "User Not Found")})
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        LOGGER.info("User found with id {} ", user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.toResponseDTO(user));
    }
}

package com.novapay.payflow_backend.user.service.impl;

import com.novapay.payflow_backend.auth.entity.enums.Role;
import com.novapay.payflow_backend.user.dto.request.KycRequest;
import com.novapay.payflow_backend.user.dto.request.UpdateUserRequest;
import com.novapay.payflow_backend.user.dto.request.UserRequest;
import com.novapay.payflow_backend.user.dto.response.UserResponse;
import com.novapay.payflow_backend.user.entity.User;
import com.novapay.payflow_backend.user.enums.UserStatus;
import com.novapay.payflow_backend.user.exception.InvalidKycStateException;
import com.novapay.payflow_backend.user.exception.UserAlreadyExistsException;
import com.novapay.payflow_backend.user.mapper.UserMapper;
import com.novapay.payflow_backend.user.repository.UserRepository;
import com.novapay.payflow_backend.user.service.UserService;
import com.novapay.payflow_backend.user.util.UserHelper;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserHelper userHelper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, UserHelper userHelper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userHelper = userHelper;
    }

    @Transactional
    @Override
    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email " + userRequest.getEmail());
        }
        User user = userMapper.toUserEntity(userRequest);
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(Role.ROLE_USER);
        User saveUser = userRepository.save(user);
        LOGGER.info("User created with id {} and email {} ", user.getId(), user.getEmail());
        return userMapper.toResponseDTO(saveUser);
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponse getUserById(Long userId) {
        User user = userHelper.findUser(userId);
        LOGGER.info("Getting User by id {} ", userId);
        return userMapper.toResponseDTO(user);
    }

    @Override
    public UserResponse updateUser(Long userId, UpdateUserRequest updateUserRequest) {
        User user = userHelper.findUser(userId);
        userMapper.updateUserFromDto(updateUserRequest, user);
        LOGGER.info("User updated with id {} ", userId);
        return userMapper.toResponseDTO(user);
    }

    @Override
    public void submitKyc(Long userId, KycRequest kycRequest) {
        LOGGER.info("KYC submission started for user {}", userId);
        User user = userHelper.findUser(userId);
        if (user.isKycVerified()) {
            throw new InvalidKycStateException("KYC has already been verified");
        }
        userMapper.updateKycFromDto(kycRequest, user);
       
        user.setStatus(UserStatus.PENDING_KYC);
        user.setKycVerified(false);
        user.setKycSubmittedAt(LocalDateTime.now());
        LOGGER.info("KYC submission successfully for user {}", userId);
    }

    @Transactional(readOnly = true)
    @Override
    public Boolean getKycStatus(Long userId) {
        User user = userHelper.findUser(userId);
        LOGGER.info("KYC status for user {}", userId);
        return user.isKycVerified();
    }

    @Override
    public void verifyKyc(Long userId) {
        LOGGER.info("Verifying KYC status for user {}", userId);
        User user = userHelper.findUser(userId);
        if (user.getStatus() != UserStatus.PENDING_KYC) {
            throw new InvalidKycStateException("KYC not submitted yet");
        }
        user.setKycVerified(true);
        user.setStatus(UserStatus.ACTIVE);
        LOGGER.info("KYC verified successfully for user {}", userId);
    }
}
package com.novapay.payflow_backend.user.service.impl;

import com.novapay.payflow_backend.user.dto.request.UpdateUserRequest;
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
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email {} " + user.getEmail());
        }
        user.setStatus(UserStatus.ACTIVE);
        User savedUser = userRepository.save(user);
        LOGGER.info("User created with id {} and email {} ", savedUser.getId(), savedUser.getEmail());
        return savedUser;
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserById(Long userId) {
        LOGGER.info("Getting User by id {}", userId);
        return userHelper.findUser(userId);
    }


    @Override
    public User updateUser(Long userId, UpdateUserRequest updateUserRequest) {
        User user = userHelper.findUser(userId);
        userMapper.updateUserFromDto(updateUserRequest, user);
        LOGGER.info("User updated with id {} ", userId);
        return user;
    }


    @Override
    public void submitKyc(Long userId) {
        LOGGER.info("KYC submission started for user {}", userId);
        User user = userHelper.findUser(userId);
        if (user.getKycVerified()) {
            throw new InvalidKycStateException("KYC has already been verified");
        }
        user.setStatus(UserStatus.PENDING_KYC);
        user.setKycVerified(false);
        LOGGER.info("KYC submission successfully for user {}", userId);
    }

    @Transactional(readOnly = true)
    @Override
    public Boolean getKycStatus(Long userId) {
        User user = userHelper.findUser(userId);
        LOGGER.info("KYC status for user {}", userId);
        return user.getKycVerified();
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
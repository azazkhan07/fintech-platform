package com.novapay.payflow_backend.user.service.impl;

import com.novapay.payflow_backend.common.exception.ResourceNotFoundException;
import com.novapay.payflow_backend.user.dto.request.UpdateUserRequest;
import com.novapay.payflow_backend.user.entity.User;
import com.novapay.payflow_backend.user.enums.UserStatus;
import com.novapay.payflow_backend.user.exception.UserAlreadyExistsException;
import com.novapay.payflow_backend.user.mapper.UserMapper;
import com.novapay.payflow_backend.user.repository.UserRepository;
import com.novapay.payflow_backend.user.service.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email {} " + user.getEmail());
        }
        user.setStatus(UserStatus.ACTIVE);
        User savedUser = userRepository.save(user);
        LOGGER.info("User created with email {} ", user.getEmail());
        return savedUser;
    }

    @Override
    public User getUserById(Long userId) {
        LOGGER.info("Getting User by id {}", userId);
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    @Override
    public User updateUser(Long userId, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        userMapper.updateUserFromDto(updateUserRequest, user);
        LOGGER.info("User updated with id {} ", userId);
        return user;
    }
}
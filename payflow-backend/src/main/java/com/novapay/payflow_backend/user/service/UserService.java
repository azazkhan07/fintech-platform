package com.novapay.payflow_backend.user.service;

import com.novapay.payflow_backend.user.dto.request.UpdateUserRequest;
import com.novapay.payflow_backend.user.entity.User;

public interface UserService {

    User createUser(User user);

    User getUserById(Long userId);

    User updateUser(Long userId, UpdateUserRequest updateUserRequest);

    void submitKyc(Long userId);

    Boolean getKycStatus(Long userId);

    void verifyKyc(Long  userId);
}

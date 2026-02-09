package com.novapay.payflow_backend.user.service;

import com.novapay.payflow_backend.user.dto.request.KycRequest;
import com.novapay.payflow_backend.user.dto.request.UpdateUserRequest;
import com.novapay.payflow_backend.user.dto.request.UserRequest;
import com.novapay.payflow_backend.user.dto.response.UserResponse;

public interface UserService {

    UserResponse createUser(UserRequest userRequest);

    UserResponse getUserById(Long userId);

    UserResponse updateUser(Long userId, UpdateUserRequest updateUserRequest);

    void submitKyc(Long userId, KycRequest kycRequest);

    Boolean getKycStatus(Long userId);

    void verifyKyc(Long  userId);
}

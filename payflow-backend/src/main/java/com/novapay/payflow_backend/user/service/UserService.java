package com.novapay.payflow_backend.user.service;

import com.novapay.payflow_backend.user.entity.User;

public interface UserService {

    User createUser(User user);

    User getUserById(Long userId);
}

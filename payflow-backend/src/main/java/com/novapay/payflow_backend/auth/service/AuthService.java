package com.novapay.payflow_backend.auth.service;

import com.novapay.payflow_backend.auth.dto.request.LoginRequest;
import com.novapay.payflow_backend.auth.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse userLogin(LoginRequest loginRequest);
}

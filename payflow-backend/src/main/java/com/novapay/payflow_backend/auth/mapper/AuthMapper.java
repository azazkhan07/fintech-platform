package com.novapay.payflow_backend.auth.mapper;

import com.novapay.payflow_backend.auth.dto.request.RegisterRequest;
import com.novapay.payflow_backend.user.dto.request.UserRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    UserRequest mapToUserRequest(RegisterRequest registerRequest);
}

package com.novapay.payflow_backend.user.mapper;

import com.novapay.payflow_backend.user.dto.request.UserRequest;
import com.novapay.payflow_backend.user.dto.response.UserResponse;
import com.novapay.payflow_backend.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponseDTO(User user);
    User toUserEntity(UserRequest userRequest);
}

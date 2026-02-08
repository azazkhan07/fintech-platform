package com.novapay.payflow_backend.user.mapper;

import com.novapay.payflow_backend.user.dto.request.UpdateUserRequest;
import com.novapay.payflow_backend.user.dto.request.UserRequest;
import com.novapay.payflow_backend.user.dto.response.UserResponse;
import com.novapay.payflow_backend.user.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "kycVerified", target = "kycVerified")
    @Mapping(source = "kycSubmittedAt", target = "kycSubmittedAt")
    UserResponse toResponseDTO(User user);

    User toUserEntity(UserRequest userRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UpdateUserRequest dto, @MappingTarget User entity);
}

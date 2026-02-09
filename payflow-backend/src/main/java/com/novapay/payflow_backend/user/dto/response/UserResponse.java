package com.novapay.payflow_backend.user.dto.response;

import com.novapay.payflow_backend.user.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record UserResponse(
        @Schema(example = "101")
        Long id,
        @Schema(example = "azazkhan.it@gmail.com")
        String email,
        @Schema(example = "Azaz Khan")
        String fullName,
        @Schema(example = "Male or Female")
        String gender,
        @Schema(example = "Age Should be 18 or Less than 99")
        int age,
        @Schema(example = "7723931413")
        String mobileNumber,
        @Schema(example = "ACTIVE OR INACTIVE, BLOCKED")
        UserStatus status,
        @Schema(example = "2026-02-06T12:30:00")
        LocalDateTime createdAt,
        @Schema(example = "false or true")
        Boolean kycVerified,
        @Schema(example = "2026-02-06T12:30:00")
        LocalDateTime kycSubmittedAt
) {
}

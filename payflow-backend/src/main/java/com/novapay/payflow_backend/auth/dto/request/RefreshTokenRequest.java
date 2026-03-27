package com.novapay.payflow_backend.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RefreshTokenRequest {
    @NotBlank(message = "Refresh access token")
    private String refreshToken;
}

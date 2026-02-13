package com.novapay.payflow_backend.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Setter;

@Setter
@Builder
public class LoginResponse {
    @Schema(example = "fngkdhdkjfsfsfu98w79w7foijfslkvnlsdvj")
    private String accessToken;
    @Schema(example = "Bearer")
    private String tokenType;
}

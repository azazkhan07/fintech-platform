package com.novapay.payflow_backend.auth.dto.response;

import com.novapay.payflow_backend.user.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {
    @Schema(example = "fngkdhdkjfsfsfu98w79w7foijfslkvnlsdvj")
    private String accessToken;
    @Schema(example = "cfsderwr7gns2dbfgnghjm8xsfsd")
    private String refreshToken;
    @Schema(example = "Azaz")
    private UserResponse  user;
}

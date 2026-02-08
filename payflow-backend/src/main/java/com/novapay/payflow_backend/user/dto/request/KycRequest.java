package com.novapay.payflow_backend.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class KycRequest {
    @Size(min = 12, max = 12, message = "Aadhar Must be 12 Digits")
    @NotBlank(message = "Aadhar number is required")
    private String aadharNumber;
    @Size(min = 10, max = 10, message = "Pan number Must be 10 Letters")
    @NotBlank(message = "PAN number is required")
    private String panNumber;
}

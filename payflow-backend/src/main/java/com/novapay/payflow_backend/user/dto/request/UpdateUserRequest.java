package com.novapay.payflow_backend.user.dto.request;

import com.novapay.payflow_backend.user.util.ValidGender;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateUserRequest {
    @NotBlank(message = "User Name is Required")
    @Size(min = 4, max = 40, message = "Name must be 4-40 characters")
    private String fullName;
    @ValidGender(message = "Only allowed male or female")
    private String gender;
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Indian mobile number")
    private String mobileNumber;
    @Min(value = 18, message = "User must be at least 18")
    @Max(value = 99, message = "Age must be below 100")
    private Integer age;
}

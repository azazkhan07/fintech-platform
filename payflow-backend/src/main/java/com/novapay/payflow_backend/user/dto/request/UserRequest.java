package com.novapay.payflow_backend.user.dto.request;


import com.novapay.payflow_backend.user.util.ValidGender;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class UserRequest {
    @NotBlank(message = "Email Must be Required")
    @Email(message = "Email is required")
    private String email;
    @NotBlank(message = "User Name is Required")
    @Size(min = 3, max = 60, message = "Name must be 3–60 characters")
    private String fullName;
    @ValidGender(message = "Only Allowed Male or Female")
    @NotBlank(message = "Gender is required")
    private String gender;
    @NotBlank(message = "Mobile Number Must be Required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
    private String mobileNumber;
    @Min(value = 18,message = "User must be at least 18")
    @Max(value = 99,message = "Age must be below 100")
    private int age;
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}

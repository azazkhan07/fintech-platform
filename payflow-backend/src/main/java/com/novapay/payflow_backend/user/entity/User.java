package com.novapay.payflow_backend.user.entity;

import com.novapay.payflow_backend.auth.entity.enums.Role;
import com.novapay.payflow_backend.user.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "users")
@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String gender;
    @Column(nullable = false)
    private int age;
    @Column(nullable = false)
    private String mobileNumber;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "kyc_verified")
    private boolean kycVerified;
    @Column(name = "kyc_submitted_at")
    private LocalDateTime kycSubmittedAt;
    @Column(name = "aadhar_number",unique = true)
    private String aadharNumber;
    @Column(name = "pan_number", unique = true)
    private String panNumber;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private boolean isEnabled = true;
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.kycVerified = false;
        this.status = UserStatus.CREATED;
    }
}
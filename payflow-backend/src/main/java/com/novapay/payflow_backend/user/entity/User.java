package com.novapay.payflow_backend.user.entity;

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
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String gender;
    @Column(nullable = false)
    private String mobileNumber;
    @Column(nullable = false)
    private Long aadhaarNumber;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @Column(nullable = false)
    private String panNumber;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

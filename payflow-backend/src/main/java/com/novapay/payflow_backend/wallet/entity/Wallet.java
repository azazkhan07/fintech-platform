package com.novapay.payflow_backend.wallet.entity;

import com.novapay.payflow_backend.wallet.entity.enums.CurrencyCode;
import com.novapay.payflow_backend.wallet.entity.enums.WalletStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallets")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "wallet_number", unique = true, nullable = false)
    private String walletNumber;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Enumerated(EnumType.STRING)
    private WalletStatus status;
    @Enumerated(EnumType.STRING)
    private CurrencyCode currency;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

package com.novapay.payflow_backend.transaction.entity;

import com.novapay.payflow_backend.transaction.entity.enums.TransactionStatus;
import com.novapay.payflow_backend.transaction.entity.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String referenceId;
    @Column(name = "sender_wallet_id", nullable = false)
    private Long senderWalletId;
    @Column(name = "receiver_wallet_id", nullable = false)
    private Long receiverWalletId;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    @Column(name = "failure_reason")
    private String failureReason;
    private String remarks;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

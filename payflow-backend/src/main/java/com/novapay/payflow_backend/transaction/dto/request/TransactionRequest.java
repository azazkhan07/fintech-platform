package com.novapay.payflow_backend.transaction.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class TransactionRequest {
    @NotNull(message = "senderwalletid is required")
    private Long senderWalletId;
    @NotNull(message = "receiverwalletid is required")
    private Long receiverWalletId;
    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be greater than 0" )
    private BigDecimal amount;
    private String remarks;
}

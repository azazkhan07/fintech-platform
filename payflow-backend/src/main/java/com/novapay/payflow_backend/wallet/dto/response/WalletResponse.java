package com.novapay.payflow_backend.wallet.dto.response;

import com.novapay.payflow_backend.wallet.entity.enums.CurrencyCode;
import com.novapay.payflow_backend.wallet.entity.enums.WalletStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "wallet response")
public record WalletResponse(
        @Schema(example = "1")
        Long id,
        @Schema(example = "WAL123456")
        String walletNumber,
        @Schema(example = "CCJ48R7V23234")
        Long userId,
        WalletStatus status,
        CurrencyCode currency
) {
}

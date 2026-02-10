package com.novapay.payflow_backend.transaction.dto.response;

import com.novapay.payflow_backend.transaction.entity.enums.TransactionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record TransactionResponse(
        @Schema(example = "ref_id:4tbsertbbg")
        String referenceId,
        @Schema(example = "1")
        BigDecimal amount,
        @Schema(example = "PENDING")
        TransactionStatus status,
        @Schema(example = "2026-02-06T12:30:00")
        LocalDateTime createdAt
) {}


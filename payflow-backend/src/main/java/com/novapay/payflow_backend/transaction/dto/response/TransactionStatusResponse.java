package com.novapay.payflow_backend.transaction.dto.response;

public record TransactionStatusResponse(
        String referenceId,
        String status
) {}
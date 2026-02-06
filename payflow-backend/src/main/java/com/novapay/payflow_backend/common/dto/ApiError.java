package com.novapay.payflow_backend.common.dto;

import java.time.LocalDateTime;

public record ApiError(int status,
                       String error,
                       String message,
                       LocalDateTime timestamp) {
}

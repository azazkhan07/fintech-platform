package com.novapay.payflow_backend.wallet.exception;

import com.novapay.payflow_backend.common.dto.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice(basePackages = "com.novapay.payflow_backend.wallet")
public class WalletExceptionHandler {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(WalletExceptionHandler.class);

    @ExceptionHandler(WalletAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleWalletExists(
            WalletAlreadyExistsException ex) {

        ApiError error = new ApiError(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.name(),
                ex.getMessage(),
                LocalDateTime.now());

        LOGGER.warn("Wallet already exists: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}

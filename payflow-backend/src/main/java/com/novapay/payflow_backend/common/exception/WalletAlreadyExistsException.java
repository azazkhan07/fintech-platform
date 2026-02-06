package com.novapay.payflow_backend.common.exception;

public class WalletAlreadyExistsException extends RuntimeException {

    public WalletAlreadyExistsException(String message) {
        super("Wallet Already Exists for userId: ");
    }
}

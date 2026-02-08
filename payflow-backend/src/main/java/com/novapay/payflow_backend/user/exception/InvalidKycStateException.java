package com.novapay.payflow_backend.user.exception;

public class InvalidKycStateException extends RuntimeException{
    public InvalidKycStateException(String message){
        super(message);
    }
}

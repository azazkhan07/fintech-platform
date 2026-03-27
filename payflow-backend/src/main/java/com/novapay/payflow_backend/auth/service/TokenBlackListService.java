package com.novapay.payflow_backend.auth.service;

public interface TokenBlackListService {
    void blacklistToken(String token);

    boolean isBlacklisted(String token);

}

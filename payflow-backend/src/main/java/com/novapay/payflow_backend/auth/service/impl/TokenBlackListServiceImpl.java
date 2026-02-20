package com.novapay.payflow_backend.auth.service.impl;

import com.novapay.payflow_backend.auth.entity.TokenBlackList;
import com.novapay.payflow_backend.auth.repository.TokenBlackListRepository;
import com.novapay.payflow_backend.auth.service.JwtService;
import com.novapay.payflow_backend.auth.service.TokenBlackListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenBlackListServiceImpl implements TokenBlackListService {

    private final TokenBlackListRepository tokenBlackListRepository;
    private final JwtService jwtService;

    @Override
    public void blacklistToken(String token) {
        LocalDateTime expirationDate = jwtService.getExpirationDate(token);

        TokenBlackList blacklist = TokenBlackList.builder()
                .token(token)
                .expiryDate(expirationDate)
                .build();
        tokenBlackListRepository.save(blacklist);
    }

    @Override
    public boolean isBlacklisted(String token) {
        return tokenBlackListRepository.existsByToken(token);
    }
}

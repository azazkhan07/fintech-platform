package com.novapay.payflow_backend.wallet.service.impl;

import com.novapay.payflow_backend.common.exception.ResourceNotFoundException;
import com.novapay.payflow_backend.common.exception.WalletAlreadyExistsException;
import com.novapay.payflow_backend.common.generator.WalletNumberGenerator;
import com.novapay.payflow_backend.wallet.entity.Wallet;
import com.novapay.payflow_backend.wallet.entity.WalletBalance;
import com.novapay.payflow_backend.wallet.entity.enums.CurrencyCode;
import com.novapay.payflow_backend.wallet.entity.enums.WalletStatus;
import com.novapay.payflow_backend.wallet.repository.WallentBalanceRepository;
import com.novapay.payflow_backend.wallet.repository.WalletRepository;
import com.novapay.payflow_backend.wallet.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
public class WalletServiceImpl implements WalletService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WalletServiceImpl.class);

    private final WalletRepository walletRepository;
    private final WallentBalanceRepository walletBalanceRepository;
    private final WalletNumberGenerator walletNumberGenerator;

    public WalletServiceImpl(WalletRepository walletRepository, WallentBalanceRepository walletBalanceRepository, WalletNumberGenerator walletNumberGenerator) {
        this.walletRepository = walletRepository;
        this.walletBalanceRepository = walletBalanceRepository;
        this.walletNumberGenerator = walletNumberGenerator;
    }

    @Override
    public Wallet createWallet(Long userId) {

        if (walletRepository.existsByUserId(userId)) {
            throw new WalletAlreadyExistsException("Wallet already exists for userId" + userId);
        }
        LOGGER.info("creating wallet for userId {}", userId);
        Wallet wallet = Wallet.builder()
                .walletNumber(walletNumberGenerator.generate())
                .userId(userId)
                .status(WalletStatus.ACTIVE)
                .currency(CurrencyCode.INR)
                .build();

        Wallet saveWallet = walletRepository.save(wallet);

        WalletBalance walletBalance = WalletBalance.builder()
                .wallet(saveWallet)
                .availableBalance(BigDecimal.ZERO)
                .blockedBalance(BigDecimal.ZERO)
                .updatedAt(LocalDateTime.now())
                .build();
        WalletBalance balance = walletBalanceRepository.save(walletBalance);
        LOGGER.info("Wallet created for userId {} walletNumber {} ", userId, saveWallet.getWalletNumber());
        return saveWallet;
    }

    @Override
    public Wallet getWalletByUserId(Long userId) {
        LOGGER.info("Get wallet by user id {}", userId);
        return walletRepository
                .findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet Not Found For UserId " + userId));
    }
}

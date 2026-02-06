package com.novapay.payflow_backend.wallet.service;

import com.novapay.payflow_backend.wallet.entity.Wallet;

public interface WalletService {

    Wallet createWallet(Long userId);

    Wallet getWalletByUserId(Long userId);
}

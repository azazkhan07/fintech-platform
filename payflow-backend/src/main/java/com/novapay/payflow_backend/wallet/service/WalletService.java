package com.novapay.payflow_backend.wallet.service;

import com.novapay.payflow_backend.wallet.dto.response.WalletResponse;

public interface WalletService {

    WalletResponse createWallet(Long userId);

    WalletResponse getWalletByUserId(Long userId);
}

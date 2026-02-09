package com.novapay.payflow_backend.common.util.validator;

import com.novapay.payflow_backend.common.exception.ResourceNotFoundException;
import com.novapay.payflow_backend.wallet.entity.Wallet;
import com.novapay.payflow_backend.wallet.repository.WalletRepository;
import org.springframework.stereotype.Component;

@Component
public class WalletHelper {

    private final WalletRepository walletRepository;

    public WalletHelper(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }
    public Wallet findWalletById(Long userId) {
        return walletRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Wallet Not Found with UserId " + userId));
    }
}

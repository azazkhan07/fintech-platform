package com.novapay.payflow_backend.wallet.repository;

import com.novapay.payflow_backend.wallet.entity.WalletBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WallentBalanceRepository extends JpaRepository<WalletBalance, Long> {

    Optional<WalletBalance> findByWalletId(Long walletId);
}

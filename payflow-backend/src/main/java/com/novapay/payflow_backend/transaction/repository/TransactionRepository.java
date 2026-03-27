package com.novapay.payflow_backend.transaction.repository;

import com.novapay.payflow_backend.transaction.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByReferenceId(String referenceId);

    Page<Transaction> findBySenderWalletIdOrReceiverWalletId(Long senderWalletId, Long receiverWalletId, Pageable pageable);
}

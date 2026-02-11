package com.novapay.payflow_backend.transaction.service;

import com.novapay.payflow_backend.transaction.dto.request.TransactionRequest;
import com.novapay.payflow_backend.transaction.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionService {
    TransactionResponse transferMoney(TransactionRequest request);

    TransactionResponse getTransactionByReferenceId(String referenceId);

    Page<TransactionResponse> getWalletTransactionHistory(Long walletId, Pageable pageable);
}

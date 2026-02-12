package com.novapay.payflow_backend.transaction.service;

import com.novapay.payflow_backend.transaction.dto.request.TransactionRequest;
import com.novapay.payflow_backend.transaction.dto.response.TransactionResponse;
import com.novapay.payflow_backend.transaction.dto.response.TransactionStatusResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    TransactionResponse transferMoney(TransactionRequest request);

    TransactionResponse getTransactionByReferenceId(String referenceId);

    Page<TransactionResponse> getWalletTransactionHistory(Long walletId, Pageable pageable);

    TransactionStatusResponse getTransactionStatusByReferenceId(String referenceId);

    TransactionResponse reverseTransaction(String referenceId);
}

package com.novapay.payflow_backend.transaction.service;

import com.novapay.payflow_backend.transaction.dto.request.TransactionRequest;
import com.novapay.payflow_backend.transaction.dto.response.TransactionResponse;

public interface TransactionService {
    TransactionResponse transferMoney(TransactionRequest request);
}

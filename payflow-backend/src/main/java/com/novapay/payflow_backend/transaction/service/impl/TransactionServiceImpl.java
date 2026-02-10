package com.novapay.payflow_backend.transaction.service.impl;

import com.novapay.payflow_backend.transaction.dto.request.TransactionRequest;
import com.novapay.payflow_backend.transaction.dto.response.TransactionResponse;
import com.novapay.payflow_backend.transaction.service.TransactionService;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Override
    public TransactionResponse transferMoney(TransactionRequest request) {
        return null;
    }
}

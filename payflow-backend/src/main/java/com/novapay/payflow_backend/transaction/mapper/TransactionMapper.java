package com.novapay.payflow_backend.transaction.mapper;

import com.novapay.payflow_backend.transaction.dto.response.TransactionResponse;
import com.novapay.payflow_backend.transaction.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionResponse toTransactionResponse(Transaction transaction);
}

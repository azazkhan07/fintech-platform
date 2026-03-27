package com.novapay.payflow_backend.transaction.service.impl;

import com.novapay.payflow_backend.common.exception.ResourceNotFoundException;
import com.novapay.payflow_backend.transaction.dto.request.TransactionRequest;
import com.novapay.payflow_backend.transaction.dto.response.TransactionResponse;
import com.novapay.payflow_backend.transaction.dto.response.TransactionStatusResponse;
import com.novapay.payflow_backend.transaction.entity.Transaction;
import com.novapay.payflow_backend.transaction.entity.enums.TransactionStatus;
import com.novapay.payflow_backend.transaction.entity.enums.TransactionType;
import com.novapay.payflow_backend.transaction.mapper.TransactionMapper;
import com.novapay.payflow_backend.transaction.repository.TransactionRepository;
import com.novapay.payflow_backend.transaction.service.TransactionService;
import com.novapay.payflow_backend.wallet.entity.WalletBalance;
import com.novapay.payflow_backend.wallet.repository.WallentBalanceRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;
    private final WallentBalanceRepository wallentBalanceRepository;
    private final TransactionMapper transactionMapper;

    @Transactional
    @Override
    public TransactionResponse transferMoney(TransactionRequest request) {

        LOGGER.info("Transfer request received | senderWalletId={} receiverWalletId={} amount={}",
                request.getSenderWalletId(), request.getReceiverWalletId(), request.getAmount());

        if (request.getSenderWalletId().equals(request.getReceiverWalletId())) {
            LOGGER.warn("Transfer failed: Sender and Receiver wallet are same | walletId={}",
                    request.getSenderWalletId());
            throw new IllegalArgumentException("Sender and Receiver cannot be same");
        }

        WalletBalance sender = wallentBalanceRepository
                .findByWalletId(request.getSenderWalletId())
                .orElseThrow(() -> {
                    LOGGER.warn("Transfer failed: Sender wallet not found | walletId={}",
                            request.getSenderWalletId());
                    return new ResourceNotFoundException("Sender wallet not found");
                });

        WalletBalance receiver = wallentBalanceRepository
                .findByWalletId(request.getReceiverWalletId())
                .orElseThrow(() -> {
                    LOGGER.warn("Transfer failed: Receiver wallet not found | walletId={}",
                            request.getReceiverWalletId());
                    return new ResourceNotFoundException("Receiver wallet not found");
                });

        BigDecimal amount = request.getAmount();

        if (sender.getAvailableBalance().compareTo(amount) < 0) {
            LOGGER.warn("Transfer failed: Insufficient balance | walletId={} balance={} attemptedAmount={}",
                    request.getSenderWalletId(),
                    sender.getAvailableBalance(),
                    amount);
            throw new IllegalArgumentException("Insufficient funds");
        }

        sender.setAvailableBalance(sender.getAvailableBalance().subtract(amount));
        receiver.setAvailableBalance(receiver.getAvailableBalance().add(amount));

        wallentBalanceRepository.save(sender);
        wallentBalanceRepository.save(receiver);

        LOGGER.info("Wallet balances updated | senderWalletId={} receiverWalletId={} amount={}",
                request.getSenderWalletId(), request.getReceiverWalletId(), amount);

        Transaction transaction = Transaction.builder()
                .senderWalletId(request.getSenderWalletId())
                .receiverWalletId(request.getReceiverWalletId())
                .amount(amount)
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.SUCCESS)
                .remarks(request.getRemarks())
                .message("Transfer Successful")
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        LOGGER.info("Transaction completed successfully | referenceId={}", savedTransaction.getReferenceId());
        return transactionMapper.toTransactionResponse(savedTransaction);
    }

    @Transactional(readOnly = true)
    @Override
    public TransactionResponse getTransactionByReferenceId(String referenceId) {
        LOGGER.info("Fetching transaction with reference {}", referenceId);
        Transaction transaction = transactionRepository
                .findByReferenceId(referenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with reference " + referenceId));

        LOGGER.info("Transaction fetched successfully | referenceId={} status={} amount={}",
                transaction.getReferenceId(),
                transaction.getStatus(),
                transaction.getAmount());
        return transactionMapper.toTransactionResponse(transaction);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TransactionResponse> getWalletTransactionHistory(Long walletId, Pageable pageable) {

        LOGGER.info("Fetching transaction history for wallet={} page={} size={}", walletId, pageable.getPageNumber(), pageable.getPageSize());

        Page<Transaction> transactionsPage = transactionRepository.findBySenderWalletIdOrReceiverWalletId(walletId, walletId, pageable);

        LOGGER.info("Wallet transaction history fetch| walletId={} totalRecords={}",
                walletId, transactionsPage.getTotalElements());

        return transactionsPage.map(transactionMapper::toTransactionResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public TransactionStatusResponse getTransactionStatusByReferenceId(String referenceId) {
        LOGGER.info("Fetching transaction status by reference id {}", referenceId);
        Transaction transaction = transactionRepository.findByReferenceId(referenceId).orElseThrow(()
                -> new ResourceNotFoundException("Transaction not found with reference id " + referenceId));
        LOGGER.info("Transaction status fetched successfully | referenceId={}", referenceId);
        return new TransactionStatusResponse(transaction.getReferenceId(), transaction.getStatus().name());
    }

    @Transactional
    @Override
    public TransactionResponse reverseTransaction(String referenceId) {

        LOGGER.info("Reversal request received with reference {}", referenceId);

        Transaction successTransaction = transactionRepository.findByReferenceId(referenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with reference id " + referenceId));

        if (successTransaction.getStatus() == TransactionStatus.REVERSED) {
            throw new IllegalStateException("Transaction already reversed with referenceId " + referenceId + successTransaction.getStatus());
        }

        if (successTransaction.getStatus() != TransactionStatus.SUCCESS) {
            throw new IllegalStateException("Only success Transaction status  can be reversed not with referenceId");
        }

        WalletBalance senderBalance = wallentBalanceRepository.findByWalletId(successTransaction.getSenderWalletId())
                .orElseThrow(() -> new ResourceNotFoundException("Sender wallet not found"));

        WalletBalance receiverBalance = wallentBalanceRepository.findByWalletId(successTransaction.getReceiverWalletId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver wallet not found"));

        BigDecimal amount = successTransaction.getAmount();

        if (receiverBalance.getAvailableBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Reversal failed: receiver has insufficient balance");
        }

        receiverBalance.setAvailableBalance(
                receiverBalance.getAvailableBalance().subtract(amount));

        senderBalance.setAvailableBalance(
                senderBalance.getAvailableBalance().add(amount));

        wallentBalanceRepository.save(senderBalance);
        wallentBalanceRepository.save(receiverBalance);

        Transaction reversalTransaction = Transaction.builder()
                .senderWalletId(successTransaction.getReceiverWalletId())
                .receiverWalletId(successTransaction.getSenderWalletId())
                .amount(amount)
                .type(TransactionType.REVERSAL)
                .status(TransactionStatus.SUCCESS)
                .message("Transaction Reversed Successfully")
                .remarks("Reversal of " + referenceId)
                .build();

        Transaction savedReversal = transactionRepository.save(reversalTransaction);

        successTransaction.setStatus(TransactionStatus.REVERSED);
        transactionRepository.save(successTransaction);

        LOGGER.info("Transaction reversed successfully | originalReferenceId={} reversalReferenceId={}", referenceId, savedReversal.getReferenceId());

        return transactionMapper.toTransactionResponse(savedReversal);
    }
}

package com.novapay.payflow_backend.transaction.controller;

import com.novapay.payflow_backend.transaction.dto.request.TransactionRequest;
import com.novapay.payflow_backend.transaction.dto.response.TransactionResponse;
import com.novapay.payflow_backend.transaction.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Transaction APIs", description = "Money transfer endpoints")
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionService transactionService;

    @Operation(summary = "Transfer money between wallets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transfer successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request / insufficient balance"),
            @ApiResponse(responseCode = "404", description = "Wallet not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transferMoney(@Valid @RequestBody TransactionRequest transactionRequest) {

        LOGGER.info("Incoming transfer request | sender={} receiver={} amount={}",
                transactionRequest.getSenderWalletId(),
                transactionRequest.getReceiverWalletId(),
                transactionRequest.getAmount());
        TransactionResponse transactionResponse = transactionService.transferMoney(transactionRequest);

        LOGGER.info("Transfer completed successfully referenceId: {}", transactionResponse.referenceId());
        return ResponseEntity.status(HttpStatus.OK).body(transactionResponse);
    }

    @Operation(summary = "Get transaction by reference id")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Transaction found"),
    @ApiResponse(responseCode = "404", description = "Transaction not found")})
    @GetMapping("/{referenceId}")
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable String referenceId) {
        TransactionResponse response = transactionService.getTransactionByReferenceId(referenceId);
        LOGGER.info("Transaction found with referenceId: {}", referenceId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

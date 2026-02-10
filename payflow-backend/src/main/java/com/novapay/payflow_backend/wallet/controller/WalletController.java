package com.novapay.payflow_backend.wallet.controller;

import com.novapay.payflow_backend.wallet.dto.request.WalletRequest;
import com.novapay.payflow_backend.wallet.dto.response.WalletResponse;
import com.novapay.payflow_backend.wallet.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Wallets APIs", description = "Wallet Management Endpoints")
@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WalletController.class);
    private  final WalletService walletService;


    public WalletController(WalletService walletService) {
        this.walletService = walletService;

    }
    @Operation(summary = "Create wallet for user")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Wallet created"),
    @ApiResponse(responseCode = "409", description = "Wallet already exists")})
    @PostMapping()
    public ResponseEntity<WalletResponse> createWallet(@Valid @RequestBody WalletRequest walletRequest) {
        LOGGER.info("Creating wallet for user {}", walletRequest.getUserId());
        WalletResponse response = walletService.createWallet(walletRequest.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @Operation(summary = "Get wallet for user")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Wallet Found"),
    @ApiResponse(responseCode = "404", description = "Wallet Not Found")})
    @GetMapping("/{userId}")
    public ResponseEntity<WalletResponse> getWallet(@PathVariable Long userId) {
        WalletResponse response = walletService.getWalletByUserId(userId);
        LOGGER.info("Retrieving wallet with id {}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

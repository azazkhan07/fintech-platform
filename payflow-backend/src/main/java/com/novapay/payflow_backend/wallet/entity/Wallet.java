package com.novapay.payflow_backend.wallet;

import jakarta.persistence.Entity;

@Entity
public class Wallet {
    private Long id;
    private  String walletNumber;
    private Long userId;
    
}

package com.novapay.payflow_backend.wallet.mapper;

import com.novapay.payflow_backend.wallet.dto.response.WalletResponse;
import com.novapay.payflow_backend.wallet.entity.Wallet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletResponse toResponseDTO(Wallet wallet);
}

package com.example.accounts.dto.requests;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    @NonNull
    private Integer receiverAccount;

    @NonNull
    private Integer senderAccount;

    @NonNull
    private BigDecimal amountInSenderCurrency;
}

package com.example.accounts.dto.responses;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetBalanceResponse {
    @NonNull
    private BigDecimal balance;

    @NonNull
    private String currency;
}

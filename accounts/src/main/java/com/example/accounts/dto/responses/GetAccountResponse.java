package com.example.accounts.dto.responses;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAccountResponse {
    @NonNull
    private BigDecimal amount;
    @NonNull
    private String currency;
}

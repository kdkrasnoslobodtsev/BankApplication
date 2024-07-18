package com.example.accounts.dto.requests;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopUpAccountRequest {
    @NonNull
    private BigDecimal amount;
}

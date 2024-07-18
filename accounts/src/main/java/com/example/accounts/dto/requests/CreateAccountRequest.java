package com.example.accounts.dto.requests;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {
    @NonNull
    private Integer customerId;

    @NonNull
    private String currency;
}

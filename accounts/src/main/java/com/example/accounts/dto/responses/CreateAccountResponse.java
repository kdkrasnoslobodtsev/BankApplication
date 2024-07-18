package com.example.accounts.dto.responses;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountResponse {
    @NonNull
    private Integer accountNumber;
}

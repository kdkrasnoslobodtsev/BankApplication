package com.example.accounts.dto.responses;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerResponse {
    @NonNull
    private Integer customerId;
}

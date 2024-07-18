package com.example.accounts.dto.requests;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequest {
    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private String birthDay;
}

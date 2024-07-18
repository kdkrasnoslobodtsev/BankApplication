package com.example.accounts.services;

import com.example.accounts.dto.responses.GetAccountResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

public interface AccountService {
    public Integer createAccount(Integer customerId, String currency);
    public GetAccountResponse getAccount(Integer customerId);
    public void topUpAccount(Integer accountNumber, BigDecimal amount);
}

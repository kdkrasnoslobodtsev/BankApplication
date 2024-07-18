package com.example.accounts.services.impl;

import com.example.accounts.dto.responses.GetAccountResponse;
import com.example.accounts.models.Account;
import com.example.accounts.repositories.AccountRepository;
import com.example.accounts.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public Integer createAccount(Integer customerId, String currency) {
        if (!currency.equals("RUB") && !currency.equals("USD")
            && !currency.equals("EUR") && !currency.equals("GBP")
            && !currency.equals("CYN")) {
            throw new IllegalArgumentException("Invalid currency");
        }
        if (accountRepository.findByCustomerIdAndCurrency(customerId, currency).isPresent()) {
            throw new IllegalArgumentException("Account already exists");
        }
        Account account = new Account(customerId, currency, BigDecimal.ZERO);
        accountRepository.save(account);
        return account.getAccountNumber();
    }

    @Override
    public GetAccountResponse getAccount(Integer accountNumber) {
        Optional<Account> account = accountRepository.findById(accountNumber);
        if (account.isEmpty()) {
            throw new IllegalArgumentException("Account not found");
        }
        return new GetAccountResponse(account.get().getAmount(), account.get().getCurrency());
    }

    @Override
    public void topUpAccount(Integer accountNumber, BigDecimal amount) {
        Optional<Account> account = accountRepository.findById(accountNumber);
        if (account.isEmpty()) {
            throw new IllegalArgumentException("Account not found");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Negative amount of top-up");
        }
        Account updated = account.get();
        updated.setAmount(updated.getAmount().add(amount));
        accountRepository.save(updated);
    }
}

package com.example.accounts.services.impl;

import com.example.accounts.controllers.WebSocketController;
import com.example.accounts.dto.responses.GetAccountResponse;
import com.example.accounts.models.Account;
import com.example.accounts.models.Outbox;
import com.example.accounts.models.WebSocketAccount;
import com.example.accounts.repositories.AccountRepository;
import com.example.accounts.repositories.OutboxRepository;
import com.example.accounts.services.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final OutboxRepository outboxRepository;


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
        simpMessagingTemplate.convertAndSend("/topic/accounts",
                new WebSocketAccount(
                        account.getAccountNumber(),
                        account.getCurrency(),
                        account.getAmount().setScale(2, RoundingMode.HALF_EVEN)
                )
        );
        return account.getAccountNumber();
    }

    @Override
    public GetAccountResponse getAccount(Integer accountNumber) {
        Account account = accountRepository.findById(accountNumber).orElseThrow(() -> new IllegalArgumentException("Account not found"));;
        return new GetAccountResponse(account.getAmount(), account.getCurrency());
    }

    @Override
    @Transactional
    public void topUpAccount(Integer accountNumber, BigDecimal amount) {
        Optional<Account> account = accountRepository.findById(accountNumber);
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Negative amount of top-up");
        }
        Account updated = account.orElseThrow(() -> new IllegalArgumentException("Account not found"));
        updated.setAmount(updated.getAmount().add(amount));
        accountRepository.save(updated);

        Outbox outbox = new Outbox(updated.getCustomerId(), "Счет " + updated.getAccountNumber() + ". Операция: +" + amount + ". Баланс: " + updated.getAmount());
        outboxRepository.save(outbox);

        simpMessagingTemplate.convertAndSend("/topic/accounts",
                new WebSocketAccount(
                        updated.getAccountNumber(),
                        updated.getCurrency(),
                        updated.getAmount().setScale(2, RoundingMode.HALF_EVEN)
                )
        );
    }
}

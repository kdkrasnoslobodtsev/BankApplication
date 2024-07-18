package com.example.accounts.services.impl;

import com.example.accounts.clients.ConverterClient;
import com.example.accounts.clients.KeycloakClient;
import com.example.accounts.repositories.AccountRepository;
import com.example.accounts.repositories.CustomerRepository;
import com.example.accounts.services.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.ConnectException;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
    private final AccountRepository accountRepository;
    private final ConverterClient converterClient;
    private final KeycloakClient keycloakClient;

    @Override
    public void makeTransfer(Integer receiverAccount, Integer senderAccount, BigDecimal amountInSenderCurrency) throws ConnectException {
        if (amountInSenderCurrency.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        var receiver = accountRepository.findById(receiverAccount);
        var sender = accountRepository.findById(senderAccount);
        if (receiver.isEmpty() || sender.isEmpty()) {
            throw new IllegalArgumentException("Receiver and Sender account must not be empty");
        }
        if (sender.get().getAmount().compareTo(amountInSenderCurrency) < 0) {
            throw new IllegalArgumentException("Sender amount is not enough");
        }
        var receiverValue = receiver.get();
        var senderValue = sender.get();
        String accessToken = keycloakClient.getOAuth2Token();
        receiverValue.setAmount(receiverValue.getAmount().add(converterClient.convert(
                senderValue.getCurrency(),
                receiverValue.getCurrency(),
                amountInSenderCurrency,
                accessToken)
        ));
        senderValue.setAmount(senderValue.getAmount().subtract(amountInSenderCurrency));
        accountRepository.save(receiverValue);
        accountRepository.save(senderValue);
    }
}

package com.example.accounts.services.impl;

import com.example.accounts.clients.ConverterClient;
import com.example.accounts.clients.KeycloakClient;
import com.example.accounts.models.Customer;
import com.example.accounts.repositories.AccountRepository;
import com.example.accounts.repositories.CustomerRepository;
import com.example.accounts.services.CustomerService;
import com.example.accounts.utils.DateChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final ConverterClient converterClient;
    private final KeycloakClient keycloakClient;

    @Override
    public Integer createCustomer(String firstName, String lastName, String birthDay) {
        Customer customer = new Customer(firstName, lastName, DateChecker.ValidateDate(birthDay));
        customerRepository.save(customer);
        return customer.getCustomerId();
    }

    @Override
    public BigDecimal getBalance(Integer customerId, String currency) {
        if (customerRepository.findById(customerId).isEmpty()) {
            throw new IllegalArgumentException("Customer not found");
        }
        // String accessToken = keycloakClient.getOAuth2Token();
        var accounts = accountRepository.findAllByCustomerId(customerId);
        BigDecimal balance = BigDecimal.ZERO;
        for (var account : accounts) {
            balance = balance.add(converterClient.convert(account.getCurrency(), currency, account.getAmount()));
        }
        return balance;
    }
}

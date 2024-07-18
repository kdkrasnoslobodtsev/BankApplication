package com.example.accounts.services;

import com.example.accounts.models.Customer;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.util.Date;

public interface CustomerService {
    public Integer createCustomer(String firstName, String lastName, String birthDay);
    public BigDecimal getBalance(Integer customerId, String currency);
}

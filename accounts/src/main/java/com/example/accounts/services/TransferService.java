package com.example.accounts.services;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.ConnectException;

public interface TransferService {
    public void makeTransfer(Integer receiverAccount, Integer senderAccount, BigDecimal amountInSenderCurrency) throws ConnectException;
}

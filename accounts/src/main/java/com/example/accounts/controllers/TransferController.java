package com.example.accounts.controllers;

import com.example.accounts.dto.requests.TransferRequest;
import com.example.accounts.errors.SimpleError;
import com.example.accounts.services.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.ConnectException;
import java.text.ParseException;

@Controller
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void makeTransfer(@RequestBody TransferRequest transferRequest) throws ConnectException {
        transferService.makeTransfer(transferRequest.getReceiverAccount(),
                transferRequest.getSenderAccount(),
                transferRequest.getAmountInSenderCurrency());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<SimpleError> handleIllegalArgumentException(ParseException e) {
        return new ResponseEntity<>(new SimpleError(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SimpleError> handleException(Exception e) {
        return new ResponseEntity<>(new SimpleError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

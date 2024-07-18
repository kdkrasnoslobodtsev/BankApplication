package com.example.accounts.controllers;

import com.example.accounts.dto.requests.CreateAccountRequest;
import com.example.accounts.dto.requests.TopUpAccountRequest;
import com.example.accounts.dto.responses.CreateAccountResponse;
import com.example.accounts.dto.responses.GetAccountResponse;
import com.example.accounts.errors.SimpleError;
import com.example.accounts.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Controller
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<CreateAccountResponse> createAccount(@RequestBody CreateAccountRequest createAccountRequest) {
        return new ResponseEntity<>(new CreateAccountResponse(
                accountService.createAccount(
                        createAccountRequest.getCustomerId(),
                        createAccountRequest.getCurrency()
                )
        ), HttpStatus.OK);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<GetAccountResponse> getAccount(@PathVariable Integer accountNumber) {
        return new ResponseEntity<>(accountService.getAccount(accountNumber), HttpStatus.OK);
    }

    @PostMapping("/{accountNumber}/top-up")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void topUpAccount(@PathVariable Integer accountNumber, @RequestBody TopUpAccountRequest topUpAccountRequest) {
        accountService.topUpAccount(accountNumber, topUpAccountRequest.getAmount());
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

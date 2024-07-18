package com.example.accounts.controllers;

import com.example.accounts.dto.requests.CreateCustomerRequest;
import com.example.accounts.dto.responses.GetBalanceResponse;
import com.example.accounts.dto.responses.CreateCustomerResponse;
import com.example.accounts.errors.SimpleError;
import com.example.accounts.services.CustomerService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.ConnectException;
import java.text.ParseException;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Controller
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CreateCustomerResponse> createCustomer(@RequestBody CreateCustomerRequest createCustomerRequest) {
        return new ResponseEntity<>(new CreateCustomerResponse(
                customerService.createCustomer(
                        createCustomerRequest.getFirstName(),
                        createCustomerRequest.getLastName(),
                        createCustomerRequest.getBirthDay()
                )
        ), HttpStatus.OK);
    }

    @GetMapping("/{customerId}/balance")
    public ResponseEntity<GetBalanceResponse> getBalance(@PathVariable Integer customerId, @NonNull @RequestParam String currency) throws ConnectException {
        return new ResponseEntity<GetBalanceResponse>(new GetBalanceResponse(
                customerService.getBalance(
                        customerId, currency
                ).setScale(2, RoundingMode.HALF_EVEN),
                currency
        ), HttpStatus.OK);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<SimpleError> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(new SimpleError(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SimpleError> handleException(Exception e) {
        return new ResponseEntity<>(new SimpleError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

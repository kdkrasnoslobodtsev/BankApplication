package com.example.accounts.controllers;

import com.example.accounts.dto.requests.CreateCustomerRequest;
import com.example.accounts.dto.responses.GetBalanceResponse;
import com.example.accounts.dto.responses.CreateCustomerResponse;
import com.example.accounts.errors.SimpleError;
import com.example.accounts.services.CustomerService;
import com.example.accounts.utils.RateLimiterGenerator;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.ConnectException;
import java.text.ParseException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

@Controller
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final RateLimiterGenerator rateLimiterGenerator;
    private final ConcurrentMap<Integer, RateLimiter> limiters = new ConcurrentHashMap<>();

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
    public ResponseEntity<GetBalanceResponse> getBalance(@PathVariable Integer customerId, @NonNull @RequestParam String currency) {
        RateLimiter rateLimiter;
        if (limiters.containsKey(customerId)) {
            rateLimiter = limiters.get(customerId);
        } else {
            rateLimiter = rateLimiterGenerator.generate(customerId);
            limiters.put(customerId, rateLimiter);
        }
        Supplier<ResponseEntity<GetBalanceResponse>> result = RateLimiter.decorateSupplier(rateLimiter,
                () -> {
                    return new ResponseEntity<GetBalanceResponse>(new GetBalanceResponse(
                            customerService.getBalance(
                                    customerId, currency
                            ),
                            currency
                    ), HttpStatus.OK);
                });
        return result.get();
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<SimpleError> handleRequestNotPermitted(RequestNotPermitted e) {
        return new ResponseEntity<>(new SimpleError(e.getMessage()), HttpStatus.TOO_MANY_REQUESTS);
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

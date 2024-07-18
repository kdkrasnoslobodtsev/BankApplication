package com.example.controllers;

import com.example.clients.KeycloakClient;
import com.example.dto.AccountResponse;
import com.example.services.ConvertService;
import com.example.errors.ErrorResponse;
import com.example.utils.JwtChecker;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;
import java.math.RoundingMode;

import java.net.ConnectException;
import java.util.Map;

@Controller
@AllArgsConstructor
public class ConvertController {
    private final ConvertService convertService;
    private final JwtChecker jwtChecker;

    @GetMapping("/convert")
    public ResponseEntity<AccountResponse> getConvertedAmount(@RequestHeader Map<String, String> headers,
                                            @RequestParam String from,
                                            @RequestParam String to,
                                            @RequestParam Double amount)
            throws ConnectException {
        System.out.println("Authorization attempt");
        jwtChecker.verifyToken(headers.get("authorization"));
        System.out.println("Authorization has gone successfully");
        BigDecimal converted = new BigDecimal(Double.toString(convertService.convert(from, to, amount)));
        converted = converted.setScale(2, RoundingMode.HALF_EVEN);
        return new ResponseEntity<AccountResponse>(new AccountResponse(to, converted), HttpStatus.OK);
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ErrorResponse> handleConnectException(ConnectException e) {
        System.out.println("ERROR: " + e.getMessage());
        return new ResponseEntity<ErrorResponse>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        System.out.println("ERROR: " + e.getMessage());
        return new ResponseEntity<ErrorResponse>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}

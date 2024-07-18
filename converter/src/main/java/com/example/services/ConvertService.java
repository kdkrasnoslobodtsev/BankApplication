package com.example.services;

import com.example.clients.KeycloakClient;
import com.example.clients.RatesClient;
import com.example.dto.OAuthToken;
import com.example.dto.Rates;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.module.FindException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class ConvertService {
    private final RatesClient ratesClient;
    private final KeycloakClient keycloakClient;

    public double convert(String from, String to, Double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Negative amount not allowed");
        }
        double fromRate = 0.0;
        double toRate = 0.0;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println("Got convert request");
            OAuthToken accessToken = keycloakClient.getOAuth2Token();
            System.out.println("Got access token: " + accessToken.getAccessToken().length());
            Rates rates = ratesClient.getRates(accessToken.getAccessToken());
            System.out.println("Got rates: " + rates.getRates().size());
            System.out.println(rates);
            fromRate = rates.getRates().get(from).doubleValue();
            toRate = rates.getRates().get(to).doubleValue();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return amount * fromRate / toRate;
    }
}

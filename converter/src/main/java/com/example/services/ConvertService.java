package com.example.services;

import com.example.clients.KeycloakClient;
import com.example.clients.RatesClient;
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
            String accessToken = keycloakClient.getOAuth2Token();
            JsonObject ratesResponse = ratesClient.getRates(accessToken);
            fromRate = Double.parseDouble(ratesResponse.getJsonObject("rates").get(from).toString());
            toRate = Double.parseDouble(ratesResponse.getJsonObject("rates").get(to).toString());
        } catch(Exception e) {
            System.out.println("Currency" + to + " unavailable");
        }
        return amount * fromRate / toRate;
    }
}

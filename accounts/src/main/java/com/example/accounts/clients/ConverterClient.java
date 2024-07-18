package com.example.accounts.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ConverterClient {
    @Value("${converter.url}")
    private String converterUrl;

    public BigDecimal convert(String from, String to, BigDecimal amount, String accessToken) throws ConnectException {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(converterUrl +"/convert?from=" + from + "&to=" + to + "&amount=" + amount);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken.substring(1, accessToken.length() - 1));
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null;) {
                    result.append(line);
                }
            }
        } catch (Exception e) {
            throw new ConnectException("Unable to connect Converter");
        }
        JsonReader reader = Json.createReader(new StringReader(result.toString()));
        JsonObject jsonObject = reader.readObject();
        return BigDecimal.valueOf(Double.parseDouble(jsonObject.get("amount").toString()));
    }
}

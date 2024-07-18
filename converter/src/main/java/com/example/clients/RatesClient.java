package com.example.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class RatesClient {
    @Value("${rates.url.get.rates}")
    private String ratesUrlGetRates;

    public JsonObject getRates(String accessToken) throws ConnectException {
        StringBuilder result = new StringBuilder();
        System.out.println("Token: " + accessToken.length());
        try {
            URL url = new URL(ratesUrlGetRates + "/rates");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken.substring(1, accessToken.length() - 1));
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    result.append(line);
                }
            }
        } catch (Exception e) {
            throw new ConnectException(e.getMessage());
        }
        JsonReader reader = Json.createReader(new StringReader(result.toString()));
        return reader.readObject();
    }
}

package com.example.accounts.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class KeycloakClient {
    @Value("${keycloak.url}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @Value("${client.id}")
    private String clientId;

    @Value("${client.secret}")
    private String clientSecret;

    public String getOAuth2Token() throws ConnectException {
        StringBuilder result = new StringBuilder();
        try {
            HttpURLConnection conn = getHttpURLConnection();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null;) {
                    result.append(line);
                }
            }
        } catch (Exception e) {
            throw new ConnectException(e.getMessage());
        }
        JsonReader reader = Json.createReader(new StringReader(result.toString()));
        JsonObject jsonObject = reader.readObject();
        return jsonObject.get("access_token").toString();
    }

    private HttpURLConnection getHttpURLConnection() throws IOException {
        String parameters = "grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret;
        byte[] postData = parameters.getBytes(StandardCharsets.UTF_8);
        URL url = new URL(keycloakUrl +"/realms/" + keycloakRealm + "/protocol/openid-connect/token");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
        conn.setUseCaches(false);
        conn.getOutputStream().write(postData);
        return conn;
    }
}

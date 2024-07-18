package com.example.clients;

import com.example.dto.OAuthToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Component
public class KeycloakClient {
    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @Value("${client.id}")
    private String clientId;

    @Value("${client.secret}")
    private String clientSecret;

    private final WebClient webClient;

    public KeycloakClient(@Value("${keycloak.url}") String keycloak_url, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(keycloak_url).build();
    }

    public OAuthToken getOAuth2Token() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var requestBody = "client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&grant_type=client_credentials";
        return webClient
                .post()
                .uri("/realms/" + keycloakRealm + "/protocol/openid-connect/token")
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(OAuthToken.class).block();
    }
}

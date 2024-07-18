package com.example.clients;

import com.example.dto.Rates;
import com.example.models.Retries;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class RatesClient {

    private final WebClient webClient;

    public RatesClient(@Value("${rates.url}") String ratesUrl, WebClient.Builder webClientBuilder) {
        webClient = webClientBuilder.baseUrl(ratesUrl).build();
    }

    private static final int MAX_ATTEMPTS = 3;
    private static final long[] DELAYS_MS = new long[] {50, 100, 150};

    public Rates getRates(String accessToken) {
        return webClient.get()
                .uri("/rates")
                .headers(headers -> {
                    headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
                })
                .retrieve()
                .bodyToMono(Rates.class)
                .retryWhen(new Retries(3,
                        Arrays.asList(Duration.ofMillis(50),
                                Duration.ofMillis(100),
                                Duration.ofMillis(150)
                        )
                )).block();
    }
}

package com.example.accounts.clients;

import com.example.accounts.models.Notification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class NotificationClient {
    private final WebClient webClient;

    public NotificationClient(@Value("${notification.service.url}") String ratesUrl, WebClient.Builder webClientBuilder) {
        webClient = webClientBuilder.baseUrl(ratesUrl).build();
    }

    public void sendNotification(Notification notification) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        webClient.post()
                .uri("/notification")
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(BodyInserters.fromValue(notification))
                .retrieve();
    }
}

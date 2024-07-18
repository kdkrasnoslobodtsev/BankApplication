package com.example.accounts.services.impl;

import com.example.accounts.clients.NotificationClient;
import com.example.accounts.models.Notification;
import com.example.accounts.models.Outbox;
import com.example.accounts.repositories.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private OutboxRepository outboxRepository;
    private NotificationClient notificationClient;

    @Scheduled(fixedDelay = 5000)
    public void sendNotifications() {
        List<Outbox> unsentMessages = outboxRepository.findBySentAtIsNull();
        for (var message : unsentMessages) {
            notificationClient.sendNotification(new Notification(message.getCustomerId(), message.getMessage()));
            message.setSentAt(LocalDateTime.now());
            outboxRepository.save(message);
        }
    }
}

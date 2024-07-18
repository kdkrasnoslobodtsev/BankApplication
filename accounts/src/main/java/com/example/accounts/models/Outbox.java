package com.example.accounts.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name = "_outbox")
@NoArgsConstructor
@Getter
@Setter
public class Outbox {
    @Id
    private Integer id;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "message")
    private String message;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    public Outbox(Integer customerId, String message) {
        this.id = ThreadLocalRandom.current().nextInt(1, 2000000000);
        this.customerId = customerId;
        this.message = message;
    }
}

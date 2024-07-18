package com.example.accounts.repositories;

import com.example.accounts.models.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxRepository extends JpaRepository<Outbox, Integer> {
    List<Outbox> findBySentAtIsNull();
}

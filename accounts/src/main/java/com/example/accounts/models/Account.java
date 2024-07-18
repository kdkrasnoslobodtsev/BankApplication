package com.example.accounts.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Getter
@Table(name = "_accounts")
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @Column(name = "account_number")
    private Integer accountNumber;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Setter
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    public Account(Integer customerId, String currency, BigDecimal amount) {
        this.accountNumber = ThreadLocalRandom.current().nextInt(1, 2000000000);
        this.customerId = customerId;
        this.currency = currency;
        this.amount = amount;
    }
}

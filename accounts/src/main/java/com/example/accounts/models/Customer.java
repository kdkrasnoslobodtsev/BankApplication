package com.example.accounts.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name = "_customers")
@NoArgsConstructor
public class Customer {
    @Id
    @Getter
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "birth_day", nullable = false)
    private Date birthDay;

    public Customer(String firstName, String lastName, Date birthDay) {
        this.customerId = ThreadLocalRandom.current().nextInt(1, 2000000000);
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
    }
}

package com.example.accounts.repositories;

import com.example.accounts.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findAllByCustomerId(Integer customerId);
    Optional<Account> findByCustomerIdAndCurrency(Integer customerId, String Currency);
}

package com.example.app.repo;

import com.example.app.entity.Transaction;
import com.example.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
}
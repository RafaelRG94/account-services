package com.example.accountservices.transaction;

import com.example.accountservices.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> { }

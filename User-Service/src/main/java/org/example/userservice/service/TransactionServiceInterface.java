package org.example.userservice.service;

import org.example.userservice.dto.TransactionDTO;

public interface TransactionServiceInterface {
    String createTransaction(String token, TransactionDTO transactionDTO);
}
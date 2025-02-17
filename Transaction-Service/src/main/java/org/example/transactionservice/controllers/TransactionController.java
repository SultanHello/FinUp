package org.example.transactionservice.controllers;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.transactionservice.model.Transaction;
import org.example.transactionservice.service.TransactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor

@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }




    @PostMapping
    public List<Transaction> transactions(){
        return transactionService.getTransactions();
    }


}

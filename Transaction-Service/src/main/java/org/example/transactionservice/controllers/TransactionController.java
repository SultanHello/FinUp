package org.example.transactionservice.controllers;


import jakarta.ws.rs.Path;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.transactionservice.model.Transaction;
import org.example.transactionservice.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor

@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
//    @GetMapping("/sumTransaction/{id}")
//    public Double getSum(@PathVariable Long id){
//        return transactionService.sum(id);
//    }

    @GetMapping("/userTransactions/{id}")
    public List<Transaction> userTransactions(@PathVariable Long id){
        return transactionService.getUserTransactions(id);
    }
    @GetMapping("/weekReport/{id}")
    public Map<String,Double>report(@PathVariable Long id){
        return transactionService.generateWeeklyReport(id);
    }

//    @GetMapping("/cotegory/{id}")
//    public
    @GetMapping("/ids")
    public List<Long> getIds(){
        return transactionService.getIds();
    }


    @GetMapping
    public List<Transaction> transactions(){
        return transactionService.getTransactions();
    }


}

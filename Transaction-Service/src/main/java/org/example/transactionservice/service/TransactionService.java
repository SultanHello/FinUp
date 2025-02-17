package org.example.transactionservice.service;


import lombok.AllArgsConstructor;
import org.example.transactionservice.config.RestConfiguration;
import org.example.transactionservice.model.Transaction;
import org.example.transactionservice.repository.TransactionRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service

@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }


    @KafkaListener(topics = "transaction", groupId = "my-group5")
    public void saveTransaction(Map<String,String> map) {
        System.out.println(123);
        transactionRepository.save(
                Transaction.builder()
                        .description(map.get("description"))
                        .amount((double) Integer.parseInt(map.get("amount")))
                        .date(map.get("date"))
                        .userId(Long.parseLong(map.get("userId")))
                        .category(getCategory())
                        .build()
        );
    }


    public String getCategory(){
        RestTemplate restTemplate= new RestTemplate();
        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        //headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://ai-service:2222/ai/category",
                HttpMethod.GET,
                entity,
                String.class
        );
        return response.getBody();
    }
}

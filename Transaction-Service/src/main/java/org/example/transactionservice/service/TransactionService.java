package org.example.transactionservice.service;


import lombok.AllArgsConstructor;
import org.example.transactionservice.client.AiClient;
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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    public final AiClient aiClient;
    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }


    @KafkaListener(topics = "transaction", groupId = "my-group5")
    public void saveTransaction(Map<String, String> map) {
        try {
            System.out.println("Received Kafka message: 123" + map);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // Проверяем, есть ли в карте все необходимые ключи
            if (!map.containsKey("description") || !map.containsKey("amount") ||
                    !map.containsKey("date") || !map.containsKey("userId")) {
                throw new IllegalArgumentException("Missing required fields in Kafka message: " + map);
            }

            // Парсим входные данные
            String description = map.get("description");
            double amount = Double.parseDouble(map.get("amount"));
            LocalDate date = LocalDate.parse(map.get("date"), formatter);
            Long userId = Long.parseLong(map.get("userId"));

            Transaction transaction = Transaction.builder()
                    .description(description)
                    .amount(amount)
                    .date(date)
                    .userId(userId)
                    .category(aiClient.getCategory(description))
                    .build();

            transactionRepository.save(transaction);

            System.out.println("Saved transaction: " + transaction);
            System.out.println(transactionRepository.findAll());

        } catch (Exception e) {
            System.err.println("Error processing Kafka message: " + map);
            e.printStackTrace(); // Логируем полную ошибку
        }
    }



    public List<Transaction> getUserTransactions(Long id){
        List<Transaction> transactions = transactionRepository.findByUserId(id);
        return transactions;



    }


    public Map<String, Double> generateWeeklyReport(Long userId) {
        // Получаем текущую дату и дату начала недели
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Извлекаем транзакции из репозитория
        List<Transaction> transactions = transactionRepository.findTransactionsByUserIdAndDateRange(
                userId, startDate, endDate);
        System.out.println(transactions);

        // Группируем транзакции по категориям и суммируем траты
        Map<String, Double> report = transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        // Печатаем отчёт для проверки
        System.out.println(report);

        return report;
    }

    public List<Long> getIds() {
        List<Long> transactionIds = transactionRepository.findAll()
                .stream()
                .map(Transaction::getUserId) // Получаем только transactionId
                .collect(Collectors.toList());
        return transactionIds;
    }

    public Map<String, Double> generateDailyReport(Long userId) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(1);
        List<Transaction> transactions = transactionRepository.findTransactionsByUserIdAndDateRange(
                userId, startDate, endDate);
        Map<String, Double> report = transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getDescription,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
        System.out.println(report);
        return report;
    }
}

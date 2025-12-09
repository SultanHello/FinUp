package org.example.transactionservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.transactionservice.client.AiClient;
import org.example.transactionservice.model.Transaction;
import org.example.transactionservice.repository.TransactionRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int DAYS_IN_WEEK = 7;

    private final TransactionRepository transactionRepository;
    private final AiClient aiClient;

    @Transactional(readOnly = true)
    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    @Transactional
    @KafkaListener(topics = "transaction", groupId = "transaction-service-group")
    public void saveTransaction(Map<String, String> transactionData) {
        try {
            log.info("Processing transaction from Kafka: {}", transactionData);

            validateTransactionData(transactionData);

            Transaction transaction = buildTransaction(transactionData);
            transactionRepository.save(transaction);

            log.info("Transaction saved successfully: id={}, userId={}, amount={}",
                    transaction.getTransactionId(), transaction.getUserId(), transaction.getAmount());

        } catch (IllegalArgumentException e) {
            log.error("Invalid transaction data: {}", transactionData, e);
            throw e;
        } catch (Exception e) {
            log.error("Failed to process transaction: {}", transactionData, e);
            throw new RuntimeException("Transaction processing failed", e);
        }
    }

    @Transactional(readOnly = true)
    public List<Transaction> getUserTransactions(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return transactionRepository.findByUserId(userId);
    }

    /**
     * ✅ ИСПРАВЛЕНО: Генерирует отчёт за ПОСЛЕДНИЕ 7 ДНЕЙ (включая сегодня)
     */
    @Transactional(readOnly = true)
    public Map<String, Double> generateWeeklyReport(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(DAYS_IN_WEEK - 1); // 7 дней включая сегодня

        log.info("Generating weekly report for user {} from {} to {}", userId, startDate, endDate);
        return generateCategoryReport(userId, startDate, endDate);
    }


    @Transactional(readOnly = true)
    public Map<String, Double> generateDailyReport(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        LocalDate today = LocalDate.now();

        // Для генерации в полночь берём ВЧЕРАШНИЙ день
        // Если нужен текущий день - используйте generateTodayReport()
        LocalDate yesterday = today.minusDays(1);

        log.info("Generating daily report for user {} for date: {}", userId, yesterday);

        List<Transaction> transactions = transactionRepository
                .findTransactionsByUserIdAndDateRange(userId, yesterday, yesterday);

        Map<String, Double> report = transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        log.info("Daily report generated for user {}: {} transactions, {} categories",
                userId, transactions.size(), report.size());

        return report;
    }


    @Transactional(readOnly = true)
    public Map<String, Double> generateTodayReport(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        LocalDate today = LocalDate.now();

        log.info("Generating today's report for user {} for date: {}", userId, today);

        List<Transaction> transactions = transactionRepository
                .findTransactionsByUserIdAndDateRange(userId, today, today);

        Map<String, Double> report = transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        log.info("Today's report generated for user {}: {} transactions", userId, transactions.size());

        return report;
    }

    @Transactional(readOnly = true)
    public List<Long> getUserIds() {
        return transactionRepository.findAll().stream()
                .map(Transaction::getUserId)
                .distinct()
                .collect(Collectors.toList());
    }

    private void validateTransactionData(Map<String, String> data) {
        List<String> requiredFields = List.of("description", "amount", "date", "userId");

        for (String field : requiredFields) {
            if (!data.containsKey(field) || data.get(field) == null || data.get(field).trim().isEmpty()) {
                throw new IllegalArgumentException("Missing or empty required field: " + field);
            }
        }

        // Валидация amount
        try {
            double amount = Double.parseDouble(data.get("amount"));
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be positive");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount format: " + data.get("amount"));
        }

        // Валидация userId
        try {
            Long.parseLong(data.get("userId"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid userId format: " + data.get("userId"));
        }
    }

    private Transaction buildTransaction(Map<String, String> data) {
        String description = data.get("description").trim();
        Double amount = Double.parseDouble(data.get("amount"));
        Long userId = Long.parseLong(data.get("userId"));

        // Парсинг даты с временем, извлечение только даты
        LocalDate date = parseTransactionDate(data.get("date"));

        // Получение категории через AI
        String category = getTransactionCategory(description);

        return Transaction.builder()
                .description(description)
                .amount(amount)
                .date(date)
                .userId(userId)
                .category(category)
                .build();
    }

    private LocalDate parseTransactionDate(String dateString) {
        try {
            // Парсим LocalDateTime и извлекаем LocalDate
            LocalDateTime dateTime = LocalDateTime.parse(dateString, DATETIME_FORMATTER);
            return dateTime.toLocalDate();
        } catch (DateTimeParseException e) {
            log.error("Failed to parse date: {}, using current date", dateString, e);
            return LocalDate.now();
        }
    }

    private String getTransactionCategory(String description) {
        try {
            String category = aiClient.getCategory(description);
            if (category == null || category.trim().isEmpty()) {
                log.warn("AI returned empty category for: {}", description);
                return "Uncategorized";
            }
            return category.trim();
        } catch (Exception e) {
            log.error("Failed to get category from AI for: {}", description, e);
            return "Uncategorized";
        }
    }

    private Map<String, Double> generateCategoryReport(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = transactionRepository
                .findTransactionsByUserIdAndDateRange(userId, startDate, endDate);

        Map<String, Double> report = transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        log.info("Category report generated for user {} ({} to {}): {} categories, {} transactions",
                userId, startDate, endDate, report.size(), transactions.size());

        return report;
    }
}
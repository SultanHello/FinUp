package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.client.AuthClient;
import org.example.userservice.dto.TransactionDTO;
import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService implements TransactionServiceInterface {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final UserService userService;
    private final AuthClient authClient;
    private final UserRepository userRepository;
    private final KafkaTemplate<String, Map<String, String>> kafkaTemplate;

    @Override
    public String createTransaction(String authHeader, TransactionDTO transactionDTO) {
        if (transactionDTO == null) {
            throw new IllegalArgumentException("Transaction data cannot be null");
        }


        if (transactionDTO.getDescription() == null || transactionDTO.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }

        try {
            String token = userService.extractToken(authHeader);
            String username = authClient.getUsernameFromAuthService(token);

            User user = userRepository.findByUsername(username);
            if (user == null) {
                throw new IllegalArgumentException("User not found: " + username);
            }

            String formattedDate = LocalDateTime.now().format(FORMATTER);

            Map<String, String> transaction = Map.of(
                    "amount", String.valueOf(transactionDTO.getAmount()),
                    "description", transactionDTO.getDescription().trim(),
                    "date", formattedDate,
                    "userId", String.valueOf(user.getId())
            );

            kafkaTemplate.send("transaction", transaction);
            log.info("Transaction sent to Kafka for user: {}, amount: {}", username, transactionDTO.getAmount());

            return "Transaction created successfully";

        } catch (Exception e) {
            log.error("Failed to create transaction", e);
            throw new RuntimeException("Failed to create transaction", e);
        }
    }
}
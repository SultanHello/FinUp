package org.example.userservice.service;

import lombok.AllArgsConstructor;
import org.example.userservice.client.AuthClient;
import org.example.userservice.dto.TransactionDTO;
import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@AllArgsConstructor
public class TransactionService implements TransactionServiceInterface {
    private final UserService userService;
    private final AuthClient authClient;
    private final UserRepository userRepository;
    private final KafkaTemplate<String, Map<String,String>> kafkaTemplate1;

    @Override
    public String createTransaction(String authHeader, TransactionDTO transactionDTO) {
        String token = userService.getToken(authHeader);
        System.out.println(123423);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = LocalDateTime.now().format(formatter);

        User user = userRepository.findByUsername(authClient.getUsernameFromAuthService(token));
        Map<String, String> transaction = Map.of(
                "amount", String.valueOf(transactionDTO.getAmount()),
                "description", transactionDTO.getDescription(),
                "date", formattedDate,
                "userId", String.valueOf(user.getId())
        );
        System.out.println(321);
        kafkaTemplate1.send("transaction", transaction);
        return "succes";
    }

}
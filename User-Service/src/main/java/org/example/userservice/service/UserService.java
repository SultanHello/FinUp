package org.example.userservice.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.userservice.model.Role;
import org.example.userservice.model.TransactionDTO;
import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository ;


    private final KafkaTemplate<String, Map<String,String>> kafkaTemplate1;
    Map<String ,String> map;
    @KafkaListener(topics = "profileInfo", groupId = "my-group4")
    public void listenToObjectMessage(Map<String,String> map) {

        this.map=map;

        User user = User.builder()
                .name(map.get("name"))
                .surname(map.get("surname"))
                .location(map.get("location"))
                .notification(true)
                .username(map.get("username"))
                .role(Role.USER)
                .build();
        userRepository.save(user);
    }

    public User getUserProfile(String token) {
        String username = getUsernameFromAuthService(token);
        System.out.println("dvsdvsdv :"+username);
        return userRepository.findByUsername(username);
    }

    public String updateUserProfile(String token, User user) {
        String username = getUsernameFromAuthService(token);
        User userOld = userRepository.findByUsername(username);
        userOld.setName(user.getName());
        userOld.setUsername(user.getUsername());
        userOld.setRole(user.getRole());
        userOld.setLocation(user.getLocation());
        userOld.setSurname(user.getSurname());
        userOld.setName(user.getName());
        userRepository.save(userOld);
        return "success updated";

    }

    private String getUsernameFromAuthService(String token){
        RestTemplate restTemplate = new RestTemplate();
        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://auth-service:8021/auth/getUsername",
                HttpMethod.GET,
                entity,
                String.class
        );
        return response.getBody();
    }
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public String setSpend(String authHeader, TransactionDTO transactionDTO) {
        String token = getToken(authHeader);
        System.out.println(123423);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = LocalDateTime.now().format(formatter);
        User user = userRepository.findByUsername(getUsernameFromAuthService(token));
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

    private String getToken(String authHeader){
        return authHeader.replace("Bearer ", "");

    }


//    public String deactivateUser(String username) {
//
//    }
//
//    public String changePassword(String username, String newPassword) {
//    }
//
//    public String confirmEmail(String username) {

//    }
}

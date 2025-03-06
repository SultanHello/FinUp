package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.client.AuthClient;
import org.example.userservice.client.BudgetClient;
import org.example.userservice.dto.ReportDTO;
import org.example.userservice.model.Role;
import org.example.userservice.dto.TransactionDTO;
import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {
    private final UserRepository userRepository ;

    private final AuthClient authClient;


    private final KafkaTemplate<String, Map<String,String>> kafkaTemplate1;

    private final BudgetClient budgetClient;
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
    @Override
    public User getUserProfile(String token) {
        String username = authClient.getUsernameFromAuthService(token);
        System.out.println("dvsdvsdv :"+username);
        return userRepository.findByUsername(username);
    }
    public Long getUserId(String token){
        String username = authClient.getUsernameFromAuthService(token);
        User user = userRepository.findByUsername(username);
        return user.getId();


    }

    @Override
    public String updateUserProfile(String token, User user) {
        String username = authClient.getUsernameFromAuthService(token);
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

    @Override
    public User getUserProfileById(Long id) {
        return null;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }





    public String getToken(String authHeader){
        return authHeader.replace("Bearer ", "");

    }
    @Override
    public List<Long> getIds() {
        return userRepository.findAll().stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }
    @Override
    public String deactivateUser(String username) {
        return null;

    }

    @Override
    public String changePassword(String username, String newPassword) {
        return null;
    }


}

package org.example.authservice.service;


import lombok.AllArgsConstructor;
import org.example.authservice.filter.JwtAuthenticationFilter;
import org.example.authservice.model.LogUser;
import org.example.authservice.model.RegUser;
import org.example.authservice.model.User;
import org.example.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager manager;
    private final PasswordEncoder passwordEncoder;

    private KafkaTemplate<String, Map<String,String>> kafkaTemplate1;

    public String register(RegUser regUser){

        User user = User.builder()
                .username(regUser.getUsername())
                .password(passwordEncoder.encode(regUser.getPassword()))
                .role(regUser.getRole())
                .build();

        userRepository.save(user);

        //тут будет логика запрос на userService для хранение профиль(храниться в другом микросервисе)
        Map<String, String> profileInfo = Map.of(
                "name", regUser.getName(),
                "surname", regUser.getSurname(),
                "username", regUser.getUsername(),
                "location", regUser.getLocation()
        );
        kafkaTemplate1.send("profileInfo", profileInfo);


        return jwtService.generateToken(user);
    }
    public String login(LogUser logUser){
        manager.authenticate(new UsernamePasswordAuthenticationToken(logUser.getUsername(),logUser.getPassword()));
        User user = userRepository.findByUsername(logUser.getUsername());
        return jwtService.generateToken(user);
    }

    public boolean isValidate(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }

        String token = authHeader.substring(7);
        return jwtService.isValid(token);
    }

    public User getInfo(String authHeader) {
        String username = jwtService.extractUsername(authHeader);
        User userInfo = userRepository.findByUsername(username);
        return userInfo;
    }

    public String getUsername(String authHeader) {
        try {
            System.out.println("DEBUG: Получен заголовок Authorization -> " + authHeader);
            String username = jwtService.extractUsername(authHeader);
            System.out.println("DEBUG: Извлечён username -> " + username);
            return username;
        } catch (Exception e) {
            e.printStackTrace(); // Выведет полный стек ошибки
            throw new RuntimeException("Error with JWT: " + e.getMessage(), e);
        }
    }
}

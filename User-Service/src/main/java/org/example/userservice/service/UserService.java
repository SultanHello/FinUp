package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.client.AuthClient;
import org.example.userservice.model.Role;
import org.example.userservice.model.User;
import org.example.userservice.repository.UserRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final AuthClient authClient;

    @Transactional
    @KafkaListener(topics = "profileInfo", groupId = "user-service-group")
    public void createUserFromKafka(Map<String, String> profileData) {
        try {
            log.info("Received profile info from Kafka: {}", profileData);

            validateProfileData(profileData);

            User user = User.builder()
                    .name(profileData.get("name"))
                    .surname(profileData.get("surname"))
                    .location(profileData.get("location"))
                    .username(profileData.get("username"))
                    .notification(true)
                    .role(Role.USER)
                    .build();

            userRepository.save(user);
            log.info("User profile created successfully: {}", user.getUsername());

        } catch (Exception e) {
            log.error("Failed to create user from Kafka message: {}", profileData, e);
            throw new RuntimeException("User profile creation failed", e);
        }
    }

    @Override
    public User getUserProfile(String token) {
        String username = extractUsernameFromToken(token);
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }

        return user;
    }

    @Override
    public User getUserProfileById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    @Override
    @Transactional
    public String updateUserProfile(String token, User updatedUser) {
        String username = extractUsernameFromToken(token);
        User existingUser = userRepository.findByUsername(username);

        if (existingUser == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }

        updateUserFields(existingUser, updatedUser);
        userRepository.save(existingUser);

        log.info("User profile updated: {}", username);
        return "Profile updated successfully";
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<Long> getIds() {
        return userRepository.findAll().stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }

    @Override
    public String deactivateUser(String username) {
        // TODO: Implement user deactivation logic
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String changePassword(String username, String newPassword) {
        // TODO: Implement password change logic (should be in auth-service)
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public Long getUserId(String token) {
        String username = extractUsernameFromToken(token);
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }

        return user.getId();
    }

    public String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }
        return authHeader.substring(7);
    }

    private String extractUsernameFromToken(String token) {
        return authClient.getUsernameFromAuthService(token);
    }

    private void validateProfileData(Map<String, String> data) {
        List<String> requiredFields = List.of("name", "surname", "username", "location");

        for (String field : requiredFields) {
            if (!data.containsKey(field) || data.get(field) == null || data.get(field).isEmpty()) {
                throw new IllegalArgumentException("Missing required field: " + field);
            }
        }
    }

    private void updateUserFields(User existing, User updated) {
        if (updated.getName() != null) {
            existing.setName(updated.getName());
        }
        if (updated.getSurname() != null) {
            existing.setSurname(updated.getSurname());
        }
        if (updated.getLocation() != null) {
            existing.setLocation(updated.getLocation());
        }
        if (updated.getRole() != null) {
            existing.setRole(updated.getRole());
        }
        // Username не должен обновляться, так как используется для идентификации
    }
}
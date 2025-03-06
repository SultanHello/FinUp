package org.example.userservice.service;

import org.example.userservice.model.User;

import java.util.List;

public interface UserServiceInterface {
    List<Long> getIds();
    User getUserProfile(String token);
    List<User> getUsers();
    String updateUserProfile(String token, User user);
    User getUserProfileById(Long id);
    String deactivateUser(String username);
    String changePassword(String username, String newPassword);

}
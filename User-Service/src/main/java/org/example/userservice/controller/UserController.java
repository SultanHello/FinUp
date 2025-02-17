package org.example.userservice.controller;


import lombok.AllArgsConstructor;
import org.example.userservice.model.TransactionDTO;
import org.example.userservice.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/getUser")
    public User getUserProfile(@RequestHeader("Authorization") String token) {
        return userService.getUserProfile(token);
    }


    @GetMapping()
    public List<User> users(){
        return userService.getUsers();
    }

    // Обновить профиль пользователя
    @PutMapping("/update")
    public String updateUserProfile( @RequestHeader("Authorization") String token, @RequestBody User user) {
        return userService.updateUserProfile(token, user);

    }
    @PostMapping("/spend")
    public String setSpending(@RequestHeader("Authorization") String token, @RequestBody TransactionDTO transactionDTO) {
        return userService.setSpend(token,transactionDTO);
    }

}

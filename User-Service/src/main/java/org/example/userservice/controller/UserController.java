package org.example.userservice.controller;


import lombok.AllArgsConstructor;
import org.example.userservice.dto.ReportDTO;
import org.example.userservice.dto.TransactionDTO;
import org.example.userservice.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.example.userservice.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user")
    public User getUserProfile(@RequestHeader("Authorization") String token) {
        return userService.getUserProfile(token);
    }
    @GetMapping("/user/{id}")
    public User getUserProfileById(@PathVariable Long id) {
        return userService.getUserProfileById(id);
    }


    @GetMapping()
    public List<User> users(){
        return userService.getUsers();
    }

    // Обновить профиль пользователя
    @PutMapping("/user/update")
    public String updateUserProfile( @RequestHeader("Authorization") String token, @RequestBody User user) {
        return userService.updateUserProfile(token, user);

    }
    @PostMapping("/user/spend")
    public String setSpending(@RequestHeader("Authorization") String token, @RequestBody TransactionDTO transactionDTO) {
        return userService.setSpend(token,transactionDTO);
    }
    @GetMapping("/id")
    public List<Long> getIds(){
        return userService.getIds();

    }


    @GetMapping("/user/report")
    public List<ReportDTO> getReport(@RequestHeader("Authorization") String token) {
        return userService.getReport(token);
    }
    @GetMapping("/user/report/last")
    public ReportDTO getReportLast(@RequestHeader("Authorization") String token) {
        return userService.getReportLast(token);
    }


}

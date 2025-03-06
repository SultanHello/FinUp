package org.example.userservice.controller;


import lombok.AllArgsConstructor;
import org.example.userservice.dto.ReportDTO;
import org.example.userservice.dto.TransactionDTO;
import org.example.userservice.model.User;
import org.example.userservice.service.ReportServiceInterface;
import org.example.userservice.service.TransactionServiceInterface;
import org.example.userservice.service.UserServiceInterface;
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
    private final UserServiceInterface userServiceInterface;
    private final TransactionServiceInterface transactionServiceInterface;
    private final ReportServiceInterface reportServiceInterface;

    @GetMapping("/user")
    public User getUserProfile(@RequestHeader("Authorization") String token) {
        return userServiceInterface.getUserProfile(token);
    }
    @GetMapping("/user/{id}")
    public User getUserProfileById(@PathVariable Long id) {
        return userServiceInterface.getUserProfileById(id);
    }


    @GetMapping()
    public List<User> users(){
        return userServiceInterface.getUsers();
    }

    // Обновить профиль пользователя
    @PutMapping("/user/update")
    public String updateUserProfile( @RequestHeader("Authorization") String token, @RequestBody User user) {
        return userServiceInterface.updateUserProfile(token, user);

    }
    @PostMapping("/user/spend")
    public String createTransaction(@RequestHeader("Authorization") String token, @RequestBody TransactionDTO transactionDTO) {
        return transactionServiceInterface.createTransaction(token,transactionDTO);
    }
    @GetMapping("/id")
    public List<Long> getIds(){
        return userServiceInterface.getIds();

    }


    @GetMapping("/user/report/weekly")
    public List<ReportDTO> getReportWeekly(@RequestHeader("Authorization") String token) {
        return reportServiceInterface.getReportWeekly(token);
    }
    @GetMapping("/user/report/weekly/last")
    public ReportDTO getReportLast(@RequestHeader("Authorization") String token) {
        return reportServiceInterface.getReportWeeklyLast(token);
    }
    @GetMapping("/user/advice/weekly")
    public String getAdviceWeekly(@RequestHeader("Authorization") String token) {
        return reportServiceInterface.getAdviceWeekly(token);
    }

    @GetMapping("/user/report/daily/last")
    public ReportDTO getReportDailyLast(@RequestHeader("Authorization") String token) {
        return reportServiceInterface.getReportDailyLast(token);
    }
    @GetMapping("/user/advice/daily")
    public String getAdviceDaily(@RequestHeader("Authorization") String token) {
        return reportServiceInterface.getAdviceDaily(token);
    }
}

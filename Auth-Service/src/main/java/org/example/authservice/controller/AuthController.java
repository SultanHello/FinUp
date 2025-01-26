package org.example.authservice.controller;


import lombok.AllArgsConstructor;
import org.example.authservice.model.RegUser;
import org.example.authservice.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/registration")
    public String registration(@RequestBody RegUser regUser) {
         return authService.register(regUser);
    }

}

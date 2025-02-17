package org.example.authservice.controller;


import lombok.AllArgsConstructor;
import org.example.authservice.model.LogUser;
import org.example.authservice.model.RegUser;
import org.example.authservice.model.User;
import org.example.authservice.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody RegUser regUser) {
        String token= authService.register(regUser);
        return ResponseEntity.ok(token);
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LogUser logUser){
        try {
            String token = authService.login(logUser);
            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid login or password");
        }
    }

    @GetMapping("/getUserInfo")
    public ResponseEntity<User> getInfo(@RequestHeader(value = "Authorization") String authHeader){
        if (authHeader == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        String token = authHeader.replace("Bearer ", "");

        User userInfo= authService.getInfo(token);
        return ResponseEntity.ok(userInfo);
    }

    @GetMapping("/getUsername")
    public ResponseEntity<String> getUsername(@RequestHeader(value = "Authorization") String authHeader){


        if (authHeader == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        String token = authHeader.replace("Bearer ", "");

        String username= authService.getUsername(token);
        return ResponseEntity.ok(username);
    }


    @PostMapping("/isValidate")
    public ResponseEntity<Boolean> isValidate(@RequestHeader(value = "Authorization") String authHeader){
        if (authHeader == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
        String token = authHeader.replace("Bearer ", "");

        boolean isValid = authService.isValidate(token);
        return ResponseEntity.ok(isValid);
    }


}

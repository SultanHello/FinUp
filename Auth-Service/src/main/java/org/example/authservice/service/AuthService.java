package org.example.authservice.service;


import lombok.AllArgsConstructor;
import org.example.authservice.filter.JwtAuthenticationFilter;
import org.example.authservice.model.LogUser;
import org.example.authservice.model.RegUser;
import org.example.authservice.model.User;
import org.example.authservice.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public String register(RegUser regUser){
        User user = User.builder()
                .username(regUser.getUsername())
                .password(regUser.getPassword())
                .role(regUser.getRole())
                .build();
        userRepository.save(user);



        return jwtService.generateToken(user);
    }
    public String login(LogUser){

    }

}

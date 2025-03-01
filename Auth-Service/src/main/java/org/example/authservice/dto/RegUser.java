package org.example.authservice.dto;


import lombok.Data;
import org.example.authservice.model.Role;

@Data
public class RegUser {
    private String name;
    private String surname;
    private String location;
    private String username;
    private String password;
    private Role role;

}

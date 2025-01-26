package org.example.authservice.model;


import lombok.Data;

@Data
public class RegUser {
    private String name;
    private String surname;
    private String location;
    private String username;
    private String password;
    private Role role;
}

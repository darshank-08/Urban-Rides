package com.example.urbanRides.DTO.User;

import lombok.Data;

import java.util.List;

@Data
public class LoginResponseDTO {

    private String token;
    private String userName;
    private List<String> roles;

    public LoginResponseDTO(String token, String userName, List<String> roles) {
        this.token = token;
        this.userName = userName;
        this.roles = roles;
    }
}

package com.example.urbanRides.DTO.User;

import lombok.Data;

@Data
public class LoginRequestDTO  {
    private String userName;
    private String password;

    public LoginRequestDTO(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}

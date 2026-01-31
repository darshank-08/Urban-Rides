package com.example.urbanRides.DTO.Admin;

import lombok.Data;

@Data
public class AdminLoginRespo {
    private String token;
    private String userName;
    private String roles;

    public AdminLoginRespo(String token, String userName, String roles) {
        this.token = token;
        this.userName = userName;
        this.roles = roles;
    }
}

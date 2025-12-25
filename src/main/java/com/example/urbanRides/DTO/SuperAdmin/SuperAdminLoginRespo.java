package com.example.urbanRides.DTO.SuperAdmin;

import lombok.Data;

@Data
public class SuperAdminLoginRespo {
    private String token;
    private String userName;
    private String roles;

    public SuperAdminLoginRespo(String token, String userName, String roles) {
        this.token = token;
        this.userName = userName;
        this.roles = roles;
    }
}

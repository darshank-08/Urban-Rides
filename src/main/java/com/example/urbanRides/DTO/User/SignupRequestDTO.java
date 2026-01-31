package com.example.urbanRides.DTO.User;

import lombok.Data;

@Data
public class SignupRequestDTO {
    private String userName;
    private String password;
    private String fullName;
    private long phoneNumber;
    private String gender;
}

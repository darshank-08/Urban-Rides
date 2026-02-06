package com.example.urbanRides.DTO.Employee;

import lombok.Data;

@Data
public class EmpSignupReqDTO {
    private String username;
    private String fullName;

    private String email;
    private long phoneNumber;

    private String gender;

    private String password;
    private String message;

}

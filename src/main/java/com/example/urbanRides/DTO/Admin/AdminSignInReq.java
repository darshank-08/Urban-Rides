package com.example.urbanRides.DTO.Admin;

import lombok.Data;

@Data
public class AdminSignInReq {

    private String adminName;
    private String adminPass;
    private String adminFullName;
    private long adminNumber;
    private String gender;
}

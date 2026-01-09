package com.example.urbanRides.DTO.Admin;

import lombok.Data;

@Data
public class AdminSignReqDTO {
    private String adminName;
    private String adminPass;
    private String adminFullName;
    private long adminNumber;
}

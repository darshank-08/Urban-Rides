package com.example.urbanRides.DTO.Admin;

import lombok.Data;

@Data
public class AdminLoginReq {
    private String adminName;
    private String adminPass;
    private String superKey;
}

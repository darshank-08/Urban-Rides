package com.example.urbanRides.DTO.SuperAdmin;

import lombok.Data;

@Data
public class SuperAdminLoginReq {
    private String adminName;
    private String adminPass;
    private String superKey;
}

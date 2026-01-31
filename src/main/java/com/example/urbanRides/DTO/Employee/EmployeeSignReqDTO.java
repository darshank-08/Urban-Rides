package com.example.urbanRides.DTO.Employee;

import lombok.Data;

@Data
public class EmployeeSignReqDTO {

    private String empName;
    private String empPass;
    private String gender;
    private String empFullName;
    private long empNumber;
}

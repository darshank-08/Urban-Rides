package com.example.urbanRides.DTO.Employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeLoginRespoDTO {

    private String token;
    private String empName;
    private String role;
}

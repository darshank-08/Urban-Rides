package com.example.urbanRides.DTO.Employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeLoginReqDTO {

    private String username;
    private String password;
}

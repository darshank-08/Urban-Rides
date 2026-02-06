
package com.example.urbanRides.DTO.Employee;

import lombok.Data;

@Data
public class EmpSignupRespo {
    private String id;
    private String username;
    private String fullName;
    private String email;
    private long phoneNumber;
    private String gender;
    private String role;
    private String status;

    public EmpSignupRespo(String userName, String role) {
    }
}

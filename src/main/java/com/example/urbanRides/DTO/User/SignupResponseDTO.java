package com.example.urbanRides.DTO.User;

import lombok.Data;
import java.util.List;

@Data
public class SignupResponseDTO {

    private String id;
    private String userName;
    private String fullName;
    private long phoneNumber;
    private List<String> roles;
}

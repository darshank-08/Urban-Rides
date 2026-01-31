package com.example.urbanRides.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Admins")
@Data
public class Admin {

    @Id
    private String id;

    @Indexed(unique = true)
    private String adminName;

    private String adminPass;
    private String adminFullName;
    private long adminNumber;
    private String gender;

    // default role
    private String role = "ADMIN";
}

package com.example.urbanRides.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Employee")
@Data
public class Employee {

    @Id
    private String id;

    @Indexed(unique = true)
    private String empName;

    private String empPass;
    private String empFullName;
    private long empNumber;
    private String gender;

    @Indexed(unique = true)
    private String email;

    private String role;
    private String status;

}

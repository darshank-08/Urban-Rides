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

    private String empFullName;

    @Indexed(unique = true)
    private String empName;

    private String empPass;
    private long empNumber;
    private boolean isEmp = false;

    private String role = "PENDING";

    private String gender;

    public void setEmp(boolean isEmp) {
        this.isEmp = isEmp;

        if (isEmp && "PENDING".equals(this.role)) {
            this.role = "EMPLOYEE";
        }
    }
}

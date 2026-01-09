package com.example.urbanRides.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Admins")
@Data
public class SuperAdmin {
    @Id
    private String id;

    @Indexed(unique = true)
    private String adminName;

    private String adminPass;
    private String adminFullName;
    private long adminNumber;
    private boolean isAdmin = false;
    private String role;

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;

        if (Boolean.TRUE.equals(isAdmin) && "PENDING".equals(this.role)) {
            this.role = "ADMIN";
        }
    }

}

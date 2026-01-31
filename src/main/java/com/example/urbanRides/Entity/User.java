package com.example.urbanRides.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("Users")
@Data
public class User {
    @Id
    private String id;

    @Indexed(unique = true)
    private String userName;

    private String password;
    private String fullName;
    private long phoneNumber;
    private List<String> roles;
    private String gender;

}

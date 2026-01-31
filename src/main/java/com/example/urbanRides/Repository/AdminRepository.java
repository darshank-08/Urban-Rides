package com.example.urbanRides.Repository;

import com.example.urbanRides.Entity.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AdminRepository extends MongoRepository<Admin, String> {

    Admin findByAdminName(String adminName);
}

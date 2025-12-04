package com.example.journalApp.Repository;

import com.example.journalApp.Entity.SuperAdmin;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SuperAdminRepository extends MongoRepository<SuperAdmin, String> {
    List<SuperAdmin> findByIsAdminFalse();

    SuperAdmin findByAdminName(String adminName);
}

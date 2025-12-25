package com.example.urbanRides.Service;

import com.example.urbanRides.Entity.SuperAdmin;
import com.example.urbanRides.Repository.SuperAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SuperAdminService {
    @Autowired
    SuperAdminRepository superAdminRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public List<SuperAdmin> getPendingAdmins() {
        return superAdminRepository.findByIsAdminFalse(); // Mongo query for isAdmin=false
    }

    public SuperAdmin findbyname(String name){
        return superAdminRepository.findByAdminName(name);
    }

    public void approveAdmin(String adminId) {
        SuperAdmin admin = superAdminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin Not Found"));

        if (!admin.isAdmin()) {
            admin.setAdmin(true);
            admin.setRole("ADMIN");
            superAdminRepository.save(admin);
        }
    }


    public void rejectAdmin(String id) {
        SuperAdmin superAdmin = superAdminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin Not Found"));

        // Only approve if not already approved
        if (!superAdmin.isAdmin()) {
            superAdminRepository.delete(superAdmin);
        }
    }

    // Delete Admin by ID
   public void DeleteAdmin(String id){
        Optional<SuperAdmin> adminOpt = superAdminRepository.findById(id);
        SuperAdmin admin = adminOpt.get();
        superAdminRepository.delete(admin);
   }

   public List<SuperAdmin> allAdmins(){
        List<SuperAdmin> admins = superAdminRepository.findAll();
        return admins;
   }

}

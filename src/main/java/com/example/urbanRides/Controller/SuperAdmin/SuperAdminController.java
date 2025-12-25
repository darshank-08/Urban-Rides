package com.example.urbanRides.Controller.SuperAdmin;

import com.example.urbanRides.Entity.SuperAdmin;
import com.example.urbanRides.Entity.User;
import com.example.urbanRides.Service.SuperAdminService;
import com.example.urbanRides.Service.OwnerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/SuperAdmin")
public class SuperAdminController {

    @Autowired
    OwnerUserService ownerUserService;

    @Autowired
    SuperAdminService superAdminService;

    @GetMapping("/Get-Users")
    public ResponseEntity<?> GetUsers(){
        List<User> All = ownerUserService.getUsers();
        return ResponseEntity.ok(All);
    }

    @GetMapping("/pending")
    public List<SuperAdmin> getPendingAdmins() {
        List<SuperAdmin> pendingSuperAdmins = superAdminService.getPendingAdmins();
        return pendingSuperAdmins;
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<?> approveAdmin(@PathVariable String id) {
        superAdminService.approveAdmin(id);
        return ResponseEntity.ok("Admin Approved Successfully!");
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<?> rejectAdmin(@PathVariable String id) {
        superAdminService.rejectAdmin(id);
        return ResponseEntity.ok("Admin Rejected Successfully!");
    }

    @DeleteMapping("/delete-admin/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable String id) {
        superAdminService.DeleteAdmin(id);
        return ResponseEntity.ok("Admin Deleted Successfully!");
    }

    @GetMapping("/all-admins")
    public ResponseEntity<List<SuperAdmin>> allAdmins(){
        List<SuperAdmin> admins = superAdminService.allAdmins(); // correct type
        return ResponseEntity.ok(admins);
    }

}

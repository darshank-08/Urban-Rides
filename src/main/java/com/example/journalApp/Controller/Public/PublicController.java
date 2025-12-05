package com.example.journalApp.Controller.Public;

import com.example.journalApp.Entity.SuperAdmin;
import com.example.journalApp.Entity.User;
import com.example.journalApp.Service.RenterUserService;
import com.example.journalApp.Service.SuperAdminService;
import com.example.journalApp.Service.OwnerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    OwnerUserService ownerUserService;

    @Autowired
    RenterUserService renterUserService;

    @Autowired
    SuperAdminService superAdminService;

    @PostMapping("/add-owner")
    public ResponseEntity<?> addOwner(@RequestBody User user){
        ownerUserService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/add-renter")
    public ResponseEntity<?> addRenter(@RequestBody User user){
        renterUserService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/add-Admin")
    public ResponseEntity<?> newAdmin(@RequestBody SuperAdmin req){
        superAdminService.registerAdmin(req);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

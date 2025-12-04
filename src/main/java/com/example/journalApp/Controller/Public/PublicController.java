package com.example.journalApp.Controller.Public;

import com.example.journalApp.Entity.SuperAdmin;
import com.example.journalApp.Entity.User;
import com.example.journalApp.Service.SuperAdminService;
import com.example.journalApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    UserService userService;

    @Autowired
    SuperAdminService superAdminService;

    @PostMapping("/add-user")
    public ResponseEntity<?> add(@RequestBody User user){
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/add-Admin")
    public ResponseEntity<?> newAdmin(@RequestBody SuperAdmin req){
        superAdminService.registerAdmin(req);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

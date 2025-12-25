package com.example.urbanRides.Controller.Public;

import com.example.urbanRides.Entity.SuperAdmin;
import com.example.urbanRides.Entity.User;
import com.example.urbanRides.Service.*;
import com.example.urbanRides.Utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sign-in")
@Slf4j
public class SignInController {

    @Autowired
    private OwnerUserService ownerUserService;

    @Autowired
    private RenterUserService renterUserService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AuthenticationManager auth;

    @Autowired
    private UserDetailServiceIMPL userDetailServicesIMPL;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/owner")
    public ResponseEntity<?> addOwner(@RequestBody User user){
        return ownerUserService.saveUser(user);
    }

    @PostMapping("/renter")
    public ResponseEntity<?> addRenter(@RequestBody User user){
        return renterUserService.saveUser(user);
    }

    @PostMapping("/admin")
    public ResponseEntity<?> newAdmin(@RequestBody SuperAdmin req){
        return adminService.registerAdmin(req);
    }

}

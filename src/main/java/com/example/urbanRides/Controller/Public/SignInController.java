package com.example.urbanRides.Controller.Public;

import com.example.urbanRides.DTO.Admin.AdminSignReqDTO;
import com.example.urbanRides.DTO.Admin.AdminSignRespoDTO;
import com.example.urbanRides.DTO.User.SignupRequestDTO;
import com.example.urbanRides.DTO.User.SignupResponseDTO;
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

import java.util.List;

@RestController
@RequestMapping("/sign-in")
@Slf4j
public class SignInController<UserRequestDTO> {

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
    public ResponseEntity<?> addOwner(@RequestBody SignupRequestDTO signupRequest) {

        // DTO -> Entity
        User user = new User();
        user.setUserName(signupRequest.getUserName());
        user.setPassword(signupRequest.getPassword()); // raw password, service will hash
        user.setFullName(signupRequest.getFullName());
        user.setPhoneNumber(signupRequest.getPhoneNumber());

        ResponseEntity<?> response = ownerUserService.saveUser(user);

        // Handle username taken
        if (!response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        // Convert saved entity -> Response DTO
        User savedUser = (User) response.getBody();
        SignupResponseDTO responseDTO = new SignupResponseDTO();
        responseDTO.setId(savedUser.getId());
        responseDTO.setUserName(savedUser.getUserName());
        responseDTO.setFullName(savedUser.getFullName());
        responseDTO.setPhoneNumber(savedUser.getPhoneNumber());
        responseDTO.setRoles(savedUser.getRoles());

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/renter")
    public ResponseEntity<?> addRenter(@RequestBody SignupRequestDTO signupRequest) {

        // DTO -> Entity
        User user = new User();
        user.setUserName(signupRequest.getUserName());
        user.setPassword(signupRequest.getPassword());
        user.setFullName(signupRequest.getFullName());
        user.setPhoneNumber(signupRequest.getPhoneNumber());

        ResponseEntity<?> response = renterUserService.saveUser(user);

        // Handle username taken or other errors
        if (!response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        // Entity -> Response DTO
        User savedUser = (User) response.getBody();

        SignupResponseDTO responseDTO = new SignupResponseDTO();
        responseDTO.setId(savedUser.getId());
        responseDTO.setUserName(savedUser.getUserName());
        responseDTO.setFullName(savedUser.getFullName());
        responseDTO.setPhoneNumber(savedUser.getPhoneNumber());
        responseDTO.setRoles(savedUser.getRoles());

        return ResponseEntity.ok(responseDTO);
    }


    @PostMapping("/admin")
    public ResponseEntity<AdminSignRespoDTO> newAdmin(
            @RequestBody AdminSignReqDTO req) {

        return adminService.registerAdmin(req);
    }


}

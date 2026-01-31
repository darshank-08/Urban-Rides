package com.example.urbanRides.Controller.Public;

import com.example.urbanRides.DTO.Admin.AdminLoginReq;
import com.example.urbanRides.DTO.Admin.AdminSignInReq;
import com.example.urbanRides.DTO.Admin.AdminSignInRespo;
import com.example.urbanRides.DTO.Employee.EmployeeSignReqDTO;
import com.example.urbanRides.DTO.Employee.EmployeeSignRespoDTO;
import com.example.urbanRides.DTO.User.SignupRequestDTO;
import com.example.urbanRides.DTO.User.SignupResponseDTO;
import com.example.urbanRides.Entity.User;
import com.example.urbanRides.Service.*;
import com.example.urbanRides.Utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sign-in")
@Slf4j
public class SignInController<UserRequestDTO> {

    @Autowired
    private OwnerUserService ownerUserService;

    @Autowired
    private RenterUserService renterUserService;

    @Autowired
    private EmployeeService employeeService;

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
        user.setPassword(signupRequest.getPassword());
        user.setFullName(signupRequest.getFullName());
        user.setPhoneNumber(signupRequest.getPhoneNumber());

        ResponseEntity<?> response = ownerUserService.saveUser(user);

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


    @PostMapping("/Employee")
    public ResponseEntity<EmployeeSignRespoDTO> newEmployee(
            @RequestBody EmployeeSignReqDTO req) {

        return employeeService.registerAdmin(req);
    }

    @PostMapping("/Admin")
    public AdminSignInRespo newAdmin(@RequestBody AdminSignInReq req){
        return adminService.create(req);
    }

}
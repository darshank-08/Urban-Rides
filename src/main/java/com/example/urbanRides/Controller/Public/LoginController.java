package com.example.urbanRides.Controller.Public;

import com.example.urbanRides.DTO.Employee.EmployeeLoginReqDTO;
import com.example.urbanRides.DTO.Admin.AdminLoginReq;
import com.example.urbanRides.DTO.User.LoginRequestDTO;
import com.example.urbanRides.DTO.User.LoginResponseDTO;
import com.example.urbanRides.Entity.Admin;
import com.example.urbanRides.Entity.Employee;
import com.example.urbanRides.Entity.User;
import com.example.urbanRides.Repository.AdminRepository;
import com.example.urbanRides.Repository.EmployeeRepository;
import com.example.urbanRides.Repository.UserRepository;
import com.example.urbanRides.Service.OwnerUserService;
import com.example.urbanRides.Service.RenterUserService;
import com.example.urbanRides.Service.UserDetailServiceIMPL;
import com.example.urbanRides.Utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    private OwnerUserService ownerUserService;

    @Autowired
    private RenterUserService renterUserService;

    @Autowired
    private AuthenticationManager auth;

    @Autowired
    private UserDetailServiceIMPL userDetailServicesIMPL;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${app.super-admin.key}")
    private String superAdminKey;


    //For Users
    @PostMapping("/user")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {

        try {
            auth.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUserName(),
                            request.getPassword()
                    )
            );

            UserDetails userDetails =
                    userDetailServicesIMPL.loadUserByUsername(request.getUserName());

            User user = userRepository.findByUserName(request.getUserName());
            if (user == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "code", "USER_NOT_FOUND",
                                "message", "User not found"
                        ));
            }

            String jwt = jwtUtils.generateToken(userDetails.getUsername());

            List<String> roles = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            return ResponseEntity.ok(
                    new LoginResponseDTO(
                            jwt,
                            userDetails.getUsername(),
                            roles
                    )
            );

        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid username or password"));
        }
    }

    //for employee
    @PostMapping("/employee")
    public ResponseEntity<?> loginEmployee(@RequestBody EmployeeLoginReqDTO request) {

        Optional<Employee> optionalEmployee =
                employeeRepository.findByEmpName(request.getEmpName());

        if (optionalEmployee.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "code", "USER_NOT_FOUND",
                            "message", "Employee not found"
                    ));
        }

        Employee employee = optionalEmployee.get();

        if (!employee.isEmp()) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "message", "Employee account is inactive"
                    ));
        }

        try {
            auth.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmpName(),
                            request.getEmpPass()
                    )
            );

            UserDetails userDetails =
                    userDetailServicesIMPL.loadUserByUsername(request.getEmpName());

            String jwt = jwtUtils.generateToken(userDetails.getUsername());

            String role = userDetails.getAuthorities()
                    .iterator()
                    .next()
                    .getAuthority();

            return ResponseEntity.ok(
                    Map.of(
                            "token", jwt,
                            "userName", userDetails.getUsername(),
                            "roles", List.of(role)
                    )
            );

        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message", "Incorrect username or password"
                    ));
        }
    }


    //for admin
    @PostMapping("/admin")
    public ResponseEntity<?> superAdminLogin(@RequestBody AdminLoginReq request) {

        try {

            Admin admin = adminRepository.findByAdminName(request.getAdminName());

            if (admin == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "code", "USER_NOT_FOUND",
                                "message", "Admin not found"
                        ));
            }

            if (!"ADMIN".equals(admin.getRole())) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of(
                                "message", "You are not ADMIN."
                        ));
            }

            auth.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getAdminName(),
                            request.getAdminPass()
                    )
            );

            if (!Objects.equals(superAdminKey, request.getSuperKey())) {
                log.error("Invalid SuperKey for admin: " + request.getAdminName());
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(Map.of(
                                "message", "Invalid SuperKey"
                        ));
            }

            UserDetails userDetails =
                    userDetailServicesIMPL.loadUserByUsername(request.getAdminName());

            String jwt = jwtUtils.generateToken(userDetails.getUsername());

            String role = userDetails.getAuthorities()
                    .stream()
                    .map(auth -> auth.getAuthority())
                    .findFirst()
                    .orElse("ROLE_ADMIN");

            return ResponseEntity.ok(
                    new LoginResponseDTO(
                            jwt,
                            userDetails.getUsername(),
                            Collections.singletonList(role)
                    )
            );

        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message", "Incorrect username or password"
                    ));
        }
    }


}

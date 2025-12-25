package com.example.urbanRides.Controller.Public;

import com.example.urbanRides.DTO.Admin.AdminLoginReqDTO;
import com.example.urbanRides.DTO.Admin.AdminLoginRespoDTO;
import com.example.urbanRides.DTO.SuperAdmin.SuperAdminLoginReq;
import com.example.urbanRides.DTO.User.LoginRequestDTO;
import com.example.urbanRides.DTO.User.LoginResponseDTO;
import com.example.urbanRides.Entity.SuperAdmin;
import com.example.urbanRides.Service.OwnerUserService;
import com.example.urbanRides.Service.RenterUserService;
import com.example.urbanRides.Service.SuperAdminService;
import com.example.urbanRides.Service.UserDetailServiceIMPL;
import com.example.urbanRides.Utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    private OwnerUserService ownerUserService;

    @Autowired
    private RenterUserService renterUserService;

    @Autowired
    private SuperAdminService superAdminService;

    @Autowired
    private AuthenticationManager auth;

    @Autowired
    private UserDetailServiceIMPL userDetailServicesIMPL;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${app.super-admin.key}")
    private String superAdminKey;

    @PostMapping("/user")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {

        try {
            auth.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUserName(),
                            request.getPassword()
                    )
            );

            UserDetails userDetails = userDetailServicesIMPL.loadUserByUsername(request.getUserName());

            String jwt = jwtUtils.generateToken(userDetails.getUsername());

            // roles extract
            List<String> roles = userDetails.getAuthorities()
                    .stream()
                    .map(auth -> auth.getAuthority())
                    .toList();

            return ResponseEntity.ok(
                    new LoginResponseDTO(
                            jwt,
                            userDetails.getUsername(),
                            roles
                    )
            );

        } catch (Exception e) {
            log.error("Error while creating token", e);
            return ResponseEntity
                    .badRequest()
                    .body("Incorrect username or password");
        }
    }

    @PostMapping("/admin")
    public ResponseEntity<?> loginAdmin(@RequestBody AdminLoginReqDTO request) {

        try {
            auth.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getAdminName(),
                            request.getAdminPass()
                    )
            );

            UserDetails userDetails = userDetailServicesIMPL.loadUserByUsername(request.getAdminName());

            String jwt = jwtUtils.generateToken(userDetails.getUsername());

            // roles extract
            String role = userDetails.getAuthorities().iterator().next().getAuthority();

            return ResponseEntity.ok(
                    new AdminLoginRespoDTO(jwt, userDetails.getUsername(), role)
            );

        } catch (Exception e) {
            log.error("Error while creating token", e);
            return ResponseEntity
                    .badRequest()
                    .body("Incorrect username or password");
        }
    }

    @PostMapping("/superadmin")
    public ResponseEntity<?> superAdminLogin(@RequestBody SuperAdminLoginReq request) {

        try {
            auth.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getAdminName(),
                            request.getAdminPass()
                    )
            );

            UserDetails userDetails = userDetailServicesIMPL
                    .loadUserByUsername(request.getAdminName());


            if (!Objects.equals(superAdminKey, request.getSuperKey())) {
                log.error("Invalid SuperKey for admin: " + request.getAdminName());
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body("Invalid SuperKey");
            }

            // 4️⃣ Generate JWT token
            String jwt = jwtUtils.generateToken(userDetails.getUsername());

            // 5️⃣ Send response
            String role = userDetails.getAuthorities()
                    .stream()
                    .map(auth -> auth.getAuthority())
                    .findFirst() // first role only
                    .orElse("ROLE_SUPERADMIN");

            return ResponseEntity.ok(
                    new LoginResponseDTO(jwt, userDetails.getUsername(), Collections.singletonList(role))
            );

        } catch (Exception e) {
            log.error("Error during SuperAdmin login", e);
            return ResponseEntity
                    .badRequest()
                    .body("Incorrect username or password");
        }
    }

}

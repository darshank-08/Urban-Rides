package com.example.journalApp.Service;


import com.example.journalApp.Entity.SuperAdmin;
import com.example.journalApp.Entity.User;
import com.example.journalApp.Repository.SuperAdminRepository;
import com.example.journalApp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class UserDetailServiceIMPL implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SuperAdminRepository superAdminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Check for normal user
        User user = userRepository.findByUserName(username);
        if (user != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUserName())
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0])) // array of roles
                    .build();
        }

        // Check for admin
        SuperAdmin superAdmin = superAdminRepository.findByAdminName(username);
        if (superAdmin != null && superAdmin.isAdmin()) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(superAdmin.getAdminName())
                    .password(superAdmin.getAdminPass())
                    .roles(superAdmin.getRole())  // ex: "Admin"
                    .build();
        }

        throw new UsernameNotFoundException("User or Admin not found with username: " + username);
    }

}
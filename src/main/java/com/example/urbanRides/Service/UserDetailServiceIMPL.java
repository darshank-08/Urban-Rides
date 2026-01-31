package com.example.urbanRides.Service;


import com.example.urbanRides.Entity.Admin;
import com.example.urbanRides.Entity.Employee;
import com.example.urbanRides.Entity.User;
import com.example.urbanRides.Repository.AdminRepository;
import com.example.urbanRides.Repository.EmployeeRepository;
import com.example.urbanRides.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class UserDetailServiceIMPL implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1️ Normal User
        User user = userRepository.findByUserName(username);
        if (user != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUserName())
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))
                    .build();
        }

        // 2️ Admin
        Admin admin = adminRepository.findByAdminName(username);
        if (admin != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(admin.getAdminName())
                    .password(admin.getAdminPass())
                    .roles(admin.getRole())
                    .build();
        }

        // 3️ Employee
        Optional<Employee> optionalEmployee =
                employeeRepository.findByEmpName(username);

        if (optionalEmployee.isPresent()) {

            Employee employee = optionalEmployee.get();

            if (employee.isEmp()) {
                return org.springframework.security.core.userdetails.User.builder()
                        .username(employee.getEmpName())
                        .password(employee.getEmpPass())
                        .roles(employee.getRole())
                        .build();
            }
        }

        throw new UsernameNotFoundException(
                "User, Admin, or Employee not found with username: " + username
        );
    }
}
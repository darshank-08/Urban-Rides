package com.example.urbanRides.Service;

import com.example.urbanRides.DTO.Admin.AdminSignInReq;
import com.example.urbanRides.DTO.Admin.AdminSignInRespo;
import com.example.urbanRides.Entity.Admin;
import com.example.urbanRides.Entity.Employee;
import com.example.urbanRides.Repository.AdminRepository;
import com.example.urbanRides.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public AdminSignInRespo create(AdminSignInReq req) {

        // DTO → Entity
        Admin admin = new Admin();
        admin.setAdminName(req.getAdminName());
        admin.setAdminPass(passwordEncoder.encode(req.getAdminPass()));
        admin.setAdminFullName(req.getAdminFullName());
        admin.setAdminNumber(req.getAdminNumber());
        admin.setGender(req.getGender());
        // role default = "ADMIN"

        // save to DB
        Admin savedAdmin = adminRepository.save(admin);

        // Entity → Response DTO
        return new AdminSignInRespo(
                savedAdmin.getAdminName(),
                savedAdmin.getRole()
        );
    }


    public List<Employee> getPendingEmployees() {
        return employeeRepository.findByIsEmpFalse();
    }

    public Optional<Employee> findByName(String name){
        return employeeRepository.findByEmpName(name);
    }

    public void approveEmployee(String empId) {
        Employee emp = employeeRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        if (!emp.isEmp()) {
            emp.setEmp(true);          // sets isEmp = true
            emp.setRole("EMPLOYEE");   // final role
            employeeRepository.save(emp);
        }
    }

    public void rejectEmployee(String empId) {
        Employee emp = employeeRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));

        if (!emp.isEmp()) {
            employeeRepository.delete(emp);
        }
    }

    public void deleteEmployee(String empId){
        employeeRepository.deleteById(empId);
    }

    public List<Employee> allEmployees(){
        return employeeRepository.findAll();
    }
}

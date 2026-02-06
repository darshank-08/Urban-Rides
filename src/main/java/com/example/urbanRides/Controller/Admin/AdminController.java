package com.example.urbanRides.Controller.Admin;

import com.example.urbanRides.DTO.Employee.EmpSignupReqDTO;
import com.example.urbanRides.Entity.Employee;
import com.example.urbanRides.Entity.User;
import com.example.urbanRides.Service.AdminService;
import com.example.urbanRides.Service.EmployeeService;
import com.example.urbanRides.Service.OwnerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Admin")
public class AdminController {

    @Autowired
    OwnerUserService ownerUserService;

    @Autowired
    AdminService adminService;

    @Autowired
    EmployeeService employeeService;

    @PostMapping("/Employee")
    public ResponseEntity<?> newEmployee(
            @RequestBody EmpSignupReqDTO req) {

        return employeeService.registerEmp(req);
    }

    @GetMapping("/Get-Users")
    public ResponseEntity<?> GetUsers(){
        List<User> All = ownerUserService.getUsers();
        return ResponseEntity.ok(All);
    }

    @DeleteMapping("/delete-employee/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable String id) {
        adminService.deleteEmployee(id);
        return ResponseEntity.ok("Admin Deleted Successfully!");
    }

    @GetMapping("/all-Employee")
    public ResponseEntity<List<Employee>> allEmployees(){
        List<Employee> Employees = adminService.allEmployees(); // correct type
        return ResponseEntity.ok(Employees);
    }

}

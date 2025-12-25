package com.example.urbanRides.Service;

import com.example.urbanRides.Entity.Car;
import com.example.urbanRides.Entity.User;
import com.example.urbanRides.Repository.CarRepository;
import com.example.urbanRides.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OwnerUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //To get all users
    public List<User> getUsers(){
        return  userRepository.findAll();
    }

    // Register or Save User (Owner)
    public ResponseEntity<?> saveUser(User user) {
        String userName = user.getUserName();
        User exists = userRepository.findByUserName(userName);

        if (exists != null){
           return ResponseEntity.badRequest().body("UserName is taken");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        List<String> defaultRoles = new ArrayList<>();
        defaultRoles.add("OWNER");
        user.setRoles(defaultRoles);
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    // Update User
    public User updateUser(String userName, User req) {
        User existing = userRepository.findByUserName(userName);

        if (existing == null) return null;

        existing.setPassword(passwordEncoder.encode(req.getPassword()));
        existing.setPhoneNumber(req.getPhoneNumber());

        return userRepository.save(existing);
    }

    public ResponseEntity<?> delete(String userName){
        // 1. Get the user
        User user = userRepository.findByUserName(userName);

        // 2. Get active cars of that owner
        List<Car> activeCars = carRepository.findByOwnerIdAndStatus(user.getId(), "ACTIVE");

        // 3. Optional: do something with active cars (e.g., block deletion if there are active cars)
        if(!activeCars.isEmpty()){
            return ResponseEntity.badRequest().body("Owner has active cars. Cannot delete account.");
        }

        // 4. Delete user
        userRepository.delete(user);
        return ResponseEntity.ok("Deleted successfully!");
    }


    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}

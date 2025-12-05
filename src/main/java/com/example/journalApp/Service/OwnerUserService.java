package com.example.journalApp.Service;

import com.example.journalApp.Entity.Car;
import com.example.journalApp.Entity.User;
import com.example.journalApp.Repository.CarRepository;
import com.example.journalApp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Register or Save User
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        List<String> defaultRoles = new ArrayList<>();
        defaultRoles.add("USER");
        user.setRoles(defaultRoles);
        return userRepository.save(user);
    }

    // Update User
    public User updateUser(String userName, User req) {
        User existing = userRepository.findByUserName(userName);

        if (existing == null) return null;

        existing.setPassword(passwordEncoder.encode(req.getPassword()));
        existing.setNumber(req.getNumber());

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

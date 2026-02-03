package com.example.urbanRides.Service;

import com.example.urbanRides.Entity.Car;
import com.example.urbanRides.Entity.User;
import com.example.urbanRides.Repository.CarRepository;
import com.example.urbanRides.Repository.UserRepository;
import com.example.urbanRides.Service.ImageServices.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OwnerUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    ImageService imageService;

    //To get all users
    public List<User> getUsers(){
        return  userRepository.findAll();
    }

    //to get user by name
    public User getUser(String userName){
        return userRepository.findByUserName(userName);
    }

    // Register or Save User (Owner)
    public ResponseEntity<?> saveUser(User user) {
        String userName = user.getUserName();
        User exists = userRepository.findByUserName(userName);

        if (exists != null) {
            // Custom error response
            Map<String, Object> errorBody = new HashMap<>();
            errorBody.put("code", "USERNAME_TAKEN");
            errorBody.put("message", "UserName is already taken");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        List<String> defaultRoles = new ArrayList<>();
        defaultRoles.add("OWNER");
        user.setRoles(defaultRoles);
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    // Update User
    public ResponseEntity<?> updateUser(String userName, User req) {
        User existing = userRepository.findByUserName(userName);

        if (existing == null) return (ResponseEntity.badRequest().body("User not found"));

        //Username
        if (req.getUserName() != null) {
            existing.setUserName(req.getGender());
        }

        //Phone
        if (req.getPhoneNumber() != null) {
            existing.setPhoneNumber(req.getPhoneNumber());
        }


        if(req.getRoles() != null){
            return ResponseEntity.badRequest().body("Connot update role");
        }

       //Fullname
        if (req.getFullName() != null) {
            existing.setFullName(req.getFullName());
        }

        //Gender
        if (req.getGender() != null) {
            existing.setGender(req.getGender());
        }

        try {
            return ResponseEntity.ok(userRepository.save(existing));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Something went Wrong");
        }

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

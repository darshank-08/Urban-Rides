package com.example.journalApp.Service;

import com.example.journalApp.Entity.User;
import com.example.journalApp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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

   public void delete(String userName){
        User user = userRepository.findByUserName(userName);
        userRepository.delete(user);
   }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}

package com.example.journalApp.Repository;

import com.example.journalApp.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

     User findByUserName(String userName);

    List<User> findByRolesContaining(String role);

}

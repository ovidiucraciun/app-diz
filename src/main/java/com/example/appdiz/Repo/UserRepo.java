package com.example.appdiz.Repo;

import com.example.appdiz.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.LinkedList;

public interface UserRepo extends JpaRepository<User, Integer> {


    @Query("SELECT user FROM User user WHERE user.username =:username  AND user.password =:password" )
    LinkedList<User> login(@Param("username") String username, @Param("password") String password);
}

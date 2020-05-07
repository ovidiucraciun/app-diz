package com.example.appdiz.Services;

import com.example.appdiz.Models.User;
import com.example.appdiz.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class Service {
    @Autowired
    UserRepo userRepo;

    public void saveUser(User user) {
        userRepo.save(user);
    }

    public boolean login(User user) {
        int id = userRepo.login(user.getUsername(), user.getPassword()).size();
        if (id > -1) return true;
        else return false;
    }
}

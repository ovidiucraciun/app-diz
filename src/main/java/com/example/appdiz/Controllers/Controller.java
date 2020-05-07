package com.example.appdiz.Controllers;

import com.example.appdiz.Models.User;
import com.example.appdiz.Repo.UserRepo;
import com.example.appdiz.Services.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@org.springframework.stereotype.Controller
@RequestMapping("/myController")
public class Controller {
    @Autowired
    Service service;

    @GetMapping("/test")
    String test(){
        User myUser =new User("ovi","ovi");

        return "ok";
    }

    @GetMapping("/home")
    String getHome(){
        return"home";
    }

    @GetMapping("/createUserPage")
    String getUserRegistration(Model model){
        User user =new User();
        model.addAttribute("user", user);
        return"registry";
    }
    @PostMapping("/createUser")
    public String createNewUser(/*@ModelAttribute("usersTable") @Valid*/ User user, BindingResult result, Model model) throws NoSuchAlgorithmException {
        if(!result.hasErrors()) {
            if (user.getUsername() != null && (user.getPassword().equals(user.getRePassword()))){
               service.saveUser(user);
            }
        }
        return "home";
    }

    @GetMapping("/loginPage")
    String getLoginPage(Model model){
        User user =new User();
        model.addAttribute("user", user);
        return"login";
    }

    @PostMapping("/login")
    public String loginUser(/*@ModelAttribute("usersTable") @Valid*/ User user, BindingResult result, Model model) throws NoSuchAlgorithmException {
        if(!result.hasErrors()) {
            if (user.getUsername() != null && user.getPassword() !=null){
               if (service.login(user)){
                   return"staticPage";
               }
            }
        }
        return "home";
    }

}

package com.gmail.iamdroal099.inventorymanagementsystem.controller;

import com.gmail.iamdroal099.inventorymanagementsystem.model.entity.User;
import com.gmail.iamdroal099.inventorymanagementsystem.service.UserService;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Data
public class SecurityController {

    private final UserService userService;

    @GetMapping("/login")
    public String showLoginPage(){
        return "login";
    }

    @GetMapping("/")
    public String main(){
        return "main";
    }

    @GetMapping("/registration")
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") User user, Model model){
        if (userService.isUserExists(user.getLogin())) {
            model.addAttribute("error", "User with this login already exists.");
            return "registration";
        } else {
            userService.saveUser(user);
            return "redirect:/login";
        }
    }

}

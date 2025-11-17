package com.example.seminarHomework.auth.controller;

import com.example.seminarHomework.core.repository.UserRepo;
import com.example.seminarHomework.core.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    @Autowired private UserRepo userRepo;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/saved")
    public String register(@ModelAttribute("user") User user,  RedirectAttributes redAttr) {
        var user1 = userRepo.findByEmail(user.getEmail());
        if (user1.isPresent()) {
            redAttr.addFlashAttribute("message", "User already exists");
            return "redirect:/register?error";
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepo.save(user);
        redAttr.addFlashAttribute("message", "User registered successfully");
        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logoutSuccess() {
        return "auth/logout";
    }
}

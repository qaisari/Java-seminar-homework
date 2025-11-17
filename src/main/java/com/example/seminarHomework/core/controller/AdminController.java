package com.example.seminarHomework.core.controller;

import com.example.seminarHomework.core.entity.User;
import com.example.seminarHomework.core.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {
    @Autowired private UserRepo userRepo;

    @GetMapping("/admin/add")
    public String AddUser(Model model) {
        model.addAttribute("user", new User());
        return "core/func/addUser";
    }
    @PostMapping("/admin/save")
    public String saveUser(@ModelAttribute User user, RedirectAttributes redAttr) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var user1 = userRepo.findByEmail(user.getEmail());
        if (user1.isPresent()) {
            redAttr.addFlashAttribute("warning", "There is already an account with that email address!");
            return "redirect:/admin/menu";
        }
        userRepo.save(user);
        redAttr.addFlashAttribute("message", "Account has been saved successfully.");
        return "redirect:/admin/menu";
    }

    @GetMapping("/admin/edit/{id}")
    public String UpdateUser(@PathVariable(name = "id") Long id, Model model) {
        if (userRepo.findById(id).isPresent()) {
            model.addAttribute("user", userRepo.findById(id).get());
        }
        return "core/func/edit";
    }
    @PostMapping(value = "/admin/update")
    public String UpdateUser(@ModelAttribute User user, RedirectAttributes redirAttr){
        if (user.getId() > 0) {
            userRepo.save(user);
        }
        redirAttr.addFlashAttribute("message", user.getName() + " is updated! ID=" + user.getId());
        return "redirect:/admin/menu";
    }

    @GetMapping("/admin/delete/{id}")
    public String DeleteUser(@PathVariable(name = "id") Long id){
        userRepo.deleteById(id);
        return "redirect:/admin/menu";
    }

    @GetMapping("/admin/menu")
    public String admin(Model model) {
        model.addAttribute("Users", userRepo.findAll());
        return "core/users/adminMenu";
    }
}

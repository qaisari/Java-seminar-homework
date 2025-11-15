package com.example.seminarHomework.core.controller;

import com.example.seminarHomework.core.entity.User;
import com.example.seminarHomework.core.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String saveUser(@ModelAttribute("user") User user, RedirectAttributes redAttr) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var user1 = userRepo.findByEmail(user.getEmail());
        if (user1.isPresent()) {
            redAttr.addFlashAttribute("message", "There is already an account with that email address Id: " + user.getId());
            return "redirect:/admin/menu";
        }
        userRepo.save(user);
        redAttr.addFlashAttribute("message", "Account has been saved successfully.");
        return "redirect:/admin/menu";
    }

    @GetMapping("/admin/edit/{id}")
    public String UpdateUser(@PathVariable(name = "id") int id, Model model) {
        model.addAttribute("user", userRepo.findById(id).get());
        return "core/func/edit";
    }
    @PostMapping("/admin/update")
    public String UpdateUser(@ModelAttribute User user){
        userRepo.save(user);
        return "redirect:/admin/menu";
    }

    @GetMapping("/admin/delete/{id}")
    public String DeleteUser(@PathVariable(name = "id") int id){
        userRepo.deleteById(id);
        return "redirect:/admin/menu";
    }

    @GetMapping("/admin/menu")
    public String admin(Model model) {
        model.addAttribute("Users", userRepo.findAll());
        return "core/users/adminMenu";
    }
}

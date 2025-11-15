package com.example.seminarHomework.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "core/index";
    }
    @GetMapping("/home")
    public String user() {
        return "core/users/user";
    }
}

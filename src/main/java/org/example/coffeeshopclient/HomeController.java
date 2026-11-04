package org.example.coffeeshopclient.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
/*
    @GetMapping("/")
    public String home(HttpSession session) {

        if (session.getAttribute("accessToken") == null) {
            return "redirect:/login";
        }

        return "redirect:/orders";
    }
*/
    @GetMapping("/")
    public String home() {
        return "redirect:/orders";
    }
}
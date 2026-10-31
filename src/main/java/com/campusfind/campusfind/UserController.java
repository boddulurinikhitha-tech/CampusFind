package com.campusfind.campusfind;

import org.springframework.stereotype.Controller;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    private final JdbcTemplate jdbc;

    public UserController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // Show registration page
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // Register user
    @PostMapping("/register")
    public String register(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            Model model) {

        try {
            jdbc.update(
                "INSERT INTO users (name, email, password) VALUES (?, ?, ?)",
                name, email, password
            );

            return "redirect:/login";

        } catch (Exception e) {
            model.addAttribute("error", "Registration failed. Email may already exist.");
            return "register";
        }
    }

    // Show login page
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Login user
    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        try {
            Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM users WHERE email = ? AND password = ?",
                Integer.class,
                email, password
            );

            if (count != null && count > 0) {
                session.setAttribute("userEmail", email);
                return "redirect:/";
            }

            model.addAttribute("error", "Invalid email or password.");
            return "login";

        } catch (Exception e) {
            model.addAttribute("error", "Login failed.");
            return "login";
        }
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}

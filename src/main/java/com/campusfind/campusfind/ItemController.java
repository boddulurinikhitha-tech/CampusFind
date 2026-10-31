package com.campusfind.campusfind;

import org.springframework.stereotype.Controller;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ItemController {

    private final JdbcTemplate jdbc;

    public ItemController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // Show report form
    @GetMapping("/report-item")
    public String reportPage() {
        return "report-item";
    }

    // Save reported item
    @PostMapping("/report-item")
    public String saveItem(
            @RequestParam String itemName,
            @RequestParam String description,
            @RequestParam String category,
            @RequestParam String location,
            @RequestParam String status,
            Model model) {

        try {
            jdbc.update(
                "INSERT INTO items " +
                "(item_name, description, category, location, status) " +
                "VALUES (?, ?, ?, ?, ?)",
                itemName, description, category, location, status
            );

            return "redirect:/items";

        } catch (Exception e) {
            model.addAttribute("error",
                "Unable to save item: " + e.getMessage());
            return "report-item";
        }
    }

    // Display all reported items
    @GetMapping("/items")
    public String showItems(Model model) {

        var items = jdbc.queryForList(
            "SELECT * FROM items ORDER BY id DESC"
        );

        model.addAttribute("items", items);

        return "items";
    }
}

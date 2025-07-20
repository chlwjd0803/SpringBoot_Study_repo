package com.example.be_prac.viewcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/passport")
    public String passport() {
        return "passport";
    }

    @GetMapping("/visa")
    public String visa() {
        return "visa";
    }

    @GetMapping("/immigration")
    public String immigration() {
        return "immigration";
    }
}

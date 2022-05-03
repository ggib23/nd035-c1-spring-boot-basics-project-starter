package com.udacity.jwdnd.course1.cloudstorage.controller;

import java.util.HashMap;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final AuthenticationService authService;

    public LoginController(AuthenticationService authService) {
        this.authService = authService;
    }

    @GetMapping()
    public String loginView() {
        return "login";
    }

    @PostMapping()
    public String signupUser(@ModelAttribute User user, Model model) {

        HashMap<String, Boolean> param = new HashMap<>();

        param.put("error", false);
        param.put("logout", false);
        
        String username = user.getUsername();
        String password = user.getPassword();

        Authentication token = authService.login(username, password);

        if (token == null) {
            param.put("error", true);
        } else {
            
            // Cookie logic, save token as cookie and redirect to homepage
        }

        return "home";
    }
}

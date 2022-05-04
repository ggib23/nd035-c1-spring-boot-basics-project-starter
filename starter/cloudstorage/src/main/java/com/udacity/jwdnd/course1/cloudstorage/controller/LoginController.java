package com.udacity.jwdnd.course1.cloudstorage.controller;

import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String loginView(HttpServletRequest request) throws ServletException {
        HashMap<String, Boolean> param = new HashMap<>();
        param.put("logout", false);
        //Authentication to check if there is any authenticated user, if yes return null
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
        
        //If auth is not null, compare the Auth Token to the token of a authenticated user
        if (auth != null && (!(auth instanceof AnonymousAuthenticationToken))) {
            param.put("logout", true);
            request.logout(); //Clears the session storage of the authorized SessionId token
        }

        return "login";
    }

    @PostMapping()
    public String loginUser(@ModelAttribute User user, Model model) {

        HashMap<String, Boolean> param = new HashMap<>();

        param.put("error", false);
        
        String username = user.getUsername();
        String password = user.getPassword();

        Authentication token = authService.login(username, password);

        if (token == null) {
            param.put("error", true);
        }

        return null;
    }
}

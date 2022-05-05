package com.udacity.jwdnd.course1.cloudstorage.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping()
    public String homeView() {
        return "home";
    }

    @PostMapping()
    public String logoutHome(HttpServletRequest request, RedirectAttributes redirectAttribute) throws ServletException {
        //Authentication to check if there is any authenticated user, if yes return null
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
        
        //If auth is not null, compare the Auth Token to the token of a authenticated user
        if (auth != null && (!(auth instanceof AnonymousAuthenticationToken))) {
            request.logout(); //Clears the session storage of the authorized SessionId token
        }

        // After redirect, flash attributes are automatically added to the model of the controller that serves the target URL
        redirectAttribute.addFlashAttribute("logout", true);
        return "redirect:/login";
    }

}

package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/credential")
public class CredentialController {
    private UserMapper userMapper;
    private CredentialService credentialService;
    
    public CredentialController(UserMapper userMapper, CredentialService credentialService) {
        this.userMapper = userMapper;
        this.credentialService = credentialService;
    }

    @PostMapping("/new")
    public String saveCredential(@ModelAttribute Credential credential, Model model, Authentication authentication, RedirectAttributes redirectAttributes) { 
        String username = authentication.getName();
        User user = userMapper.getUser(username);

        if (credential.getCredentialId() != 0) {
            try {
                // Update an existing credential
                credentialService.updateCredential(credential);
                // After redirect, flash attributes are automatically added to the model of the controller that serves the target URL
                redirectAttributes.addFlashAttribute("successMessage", "Success! You have saved your credential successfully.");
                return "redirect:/result";
            } catch (Exception error) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: Please try again.");
                return "redirect:/result";
            }
        } else {
            try {
                // Creating a new credential
                credential.setUserId(user.getUserId());
                credentialService.createCredential(credential);
                // After redirect, flash attributes are automatically added to the model of the controller that serves the target URL
                redirectAttributes.addFlashAttribute("successMessage", "Success! You have saved your credential successfully.");
                return "redirect:/result";
            } catch (Exception error) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: Please try again.");
                return "redirect:/result";
            }
        }
    }

    @GetMapping("/delete/{credentialId}")
    public String deleteCredential(@PathVariable Integer credentialId, RedirectAttributes redirectAttributes){
        try {
            credentialService.deleteCredential(credentialId);
            redirectAttributes.addFlashAttribute("successMessage", "Success! Your credential was deleted.");
            return "redirect:/result";
        } catch (Exception error){
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Please try again.");
            return "redirect:/result";
        }
    }
}

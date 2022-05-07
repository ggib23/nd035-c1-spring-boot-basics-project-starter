package com.udacity.jwdnd.course1.cloudstorage.controller;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/home")
public class HomeController {

    private UserMapper userMapper;
    private NoteService noteService;
    private FileService fileService;
    private CredentialService credentialsService;
    private EncryptionService encryptionService;

    public HomeController(UserMapper userMapper, NoteService noteService, FileService fileService, CredentialService credentialsService, EncryptionService encryptionService) {
        this.userMapper = userMapper;
        this.noteService = noteService;
        this.fileService = fileService;
        this.credentialsService = credentialsService;
        this.encryptionService = encryptionService;
    }

    @GetMapping()
    public String homeView(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userMapper.getUser(username);
        Integer userId = user.getUserId();
        Note note = new Note(0, "", "", userId);
        Credential credential = new Credential(0, "", "", "", "", userId);

        List<Note> notes = noteService.getNotes(userId);
        List<Credential> credentials = credentialsService.getCredentials(userId);
        model.addAttribute("notesList", notes);
        model.addAttribute("noteDetails", note);
        model.addAttribute("filesList", fileService.getFiles(userId));
        model.addAttribute("credentialsList", credentials);
        model.addAttribute("credentialDetails", credential);
        model.addAttribute("encryptionService", encryptionService);

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

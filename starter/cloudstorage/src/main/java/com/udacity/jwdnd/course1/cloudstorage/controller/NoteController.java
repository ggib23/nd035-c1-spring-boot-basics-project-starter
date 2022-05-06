package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;

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
@RequestMapping("/note")
public class NoteController {
    private UserMapper userMapper;
    private NoteService noteService;

    public NoteController(UserMapper userMapper, NoteService noteService) {
        this.userMapper = userMapper;
        this.noteService = noteService;
    }

    @PostMapping("/new")
    public String saveNote(@ModelAttribute Note note, Model model, Authentication authentication, RedirectAttributes redirectAttributes) { 
        String username = authentication.getName();
        User user = userMapper.getUser(username);

        if (note.getNoteId() != null) {
            try {
                // Update an existing note
                noteService.updateNote(note);
                // After redirect, flash attributes are automatically added to the model of the controller that serves the target URL
                redirectAttributes.addFlashAttribute("successMessage", "Success! You have saved your note successfully.");
                return "redirect:/result";
            } catch (Exception error) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: Please try again.");
                return "redirect:/result";
            }
        } else {
            try {
                // Creating a new note
                note.setUserId(user.getUserId());
                noteService.createNote(note);
                // After redirect, flash attributes are automatically added to the model of the controller that serves the target URL
                redirectAttributes.addFlashAttribute("successMessage", "Success! You have saved your note successfully.");
                return "redirect:/result";
            } catch (Exception error) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: Please try again.");
                return "redirect:/result";
            }
        }
    }

    @GetMapping("/delete/{noteId}")
    public String deleteNote(@PathVariable Integer noteId, RedirectAttributes redirectAttributes){
        try {
            noteService.deleteNote(noteId);
            redirectAttributes.addFlashAttribute("successMessage", "Success! Your note was deleted.");
            return "redirect:/result";
        } catch (Exception error){
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Please try again.");
            return "redirect:/result";
        }
    }
}

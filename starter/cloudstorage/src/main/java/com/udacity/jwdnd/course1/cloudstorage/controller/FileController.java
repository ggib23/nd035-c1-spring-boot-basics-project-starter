package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/file")
public class FileController {
    private UserMapper userMapper;
    private FileService fileService;
    private Logger logger = LoggerFactory.getLogger(FileController.class);

    public FileController (UserMapper userMapper, FileService fileService) {
        this.userMapper = userMapper;
        this.fileService = fileService;
    }

    @PostMapping("/new")
    public String saveFile(@RequestParam("fileUpload") MultipartFile multipartFile, File file, Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        User user = userMapper.getUser(username);
        file.setUserId(user.getUserId());

        if (fileService.getFileByName(multipartFile.getOriginalFilename()) != null) {
            // Redirect, as file name already exists
            // After redirect, flash attributes are automatically added to the model of the controller that serves the target URL
            redirectAttributes.addFlashAttribute("errorMessage", "File name already exists! Please try again.");
            return "redirect:/result";
        } else if (multipartFile.getOriginalFilename().isEmpty()) {
            // Redirect, as no file was selected
            // After redirect, flash attributes are automatically added to the model of the controller that serves the target URL
            redirectAttributes.addFlashAttribute("errorMessage", "Oops! You did not select a file! Please try again.");
            return "redirect:/result";
        } else {
            try {
                // Creating a new file
                fileService.createFile(file, multipartFile);
                // After redirect, flash attributes are automatically added to the model of the controller that serves the target URL
                redirectAttributes.addFlashAttribute("successMessage", "Success! You have saved your file successfully.");
                return "redirect:/result";
            } catch (Exception error) {
                logger.error(error.getMessage());
                redirectAttributes.addFlashAttribute("errorMessage", "Error: Please try again.");
                return "redirect:/result";
            }
        }
    }

    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable Integer fileId, RedirectAttributes redirectAttributes) {
        try {
            fileService.deleteFile(fileId);
            redirectAttributes.addFlashAttribute("successMessage", "Success! Your credential was deleted.");
            return "redirect:/result";
        } catch (Exception error){
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Please try again.");
            return "redirect:/result";
        }
    }

    @GetMapping("/view/{fileId}")
    public ResponseEntity<Resource> viewFile(@PathVariable Integer fileId) {
        try {
            File file = fileService.getFileById(fileId);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                    .body(new ByteArrayResource(file.getFileData()));

        } catch (Exception error){
            logger.error("Cause: " + error.getCause() + ". Message: " + error.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}

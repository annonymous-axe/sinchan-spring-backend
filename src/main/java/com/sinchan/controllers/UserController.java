package com.sinchan.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sinchan.entities.User;
import com.sinchan.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/user/profile")
	public String userProfile(@RequestParam("email") String email, Model model) {
		
		User user = userService.findUserByEmail(email);
		
		model.addAttribute("user", user);		
		
		return "pages/user";
		
	}

	@PostMapping("/user/update")
	public String updateUser(@ModelAttribute("user") User user, 
							 @RequestParam(name = "imageFile", required = false) MultipartFile imageFile, 
							 HttpServletRequest request) {
		
	    if (imageFile!=null && !imageFile.isEmpty()) {
	        try {
	            String uploadDir = new ClassPathResource("static/images").getFile().getAbsolutePath();
	            Path uploadPath = Paths.get(uploadDir);

	            // Overwrite as logo.png
	            Files.write(uploadPath.resolve("logo.png"), imageFile.getBytes(), StandardOpenOption.CREATE);
	        } catch (IOException e) {
	            e.printStackTrace();
	            // Handle error
	        }
	    }		
		
		userService.update(user);
		
		return "pages/home";
		
	}
}

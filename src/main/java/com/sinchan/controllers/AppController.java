package com.sinchan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sinchan.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AppController {
	
	private final UserService userService;
	
	public AppController(UserService userService) {
		this.userService = userService;
	}
	
//	@GetMapping("/")
//	public String home(@AuthenticationPrincipal UserDetails userDetails, HttpSession session) {
//
//		User user = userService.findUserByEmail(userDetails.getUsername());
//
//		session.setAttribute("user", user);
//
//		return "pages/home";
//	}
	
//	@GetMapping("/login")
//	public String login(@RequestParam(name = "error", required = false)String error, Model model){
//
//		model.addAttribute("error", error);
//
//		return "pages/login";
//	}

}

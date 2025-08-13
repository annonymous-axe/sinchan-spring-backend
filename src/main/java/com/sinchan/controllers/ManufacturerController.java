package com.sinchan.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sinchan.entities.Manufacturers;
import com.sinchan.entities.User;
import com.sinchan.services.ManufacturersService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
public class ManufacturerController {
	
	private final ManufacturersService manufacturerService;
	
	public ManufacturerController(ManufacturersService manufacturerService) {
		this.manufacturerService = manufacturerService;
	}
	
	@GetMapping("/manufacturer/view")
	public String manufacturerView(Model model, HttpSession session) {
		
		User user = (User) session.getAttribute("user");
		
		model.addAttribute("manufacturerList", manufacturerService.listManufacturers(user.getUserId()));
		
		return "pages/manufacturerView";
	}
	
	@PostMapping("/manufacturer/save")
	public String manufacturerSave(@RequestParam("formData")String manufacturerName, Model model, HttpSession session) {
		
		User user = (User) session.getAttribute("user");
		
		Manufacturers manufacturer = new Manufacturers();
		
		manufacturer.setName(manufacturerName);
		
		if(manufacturerService.exist(manufacturerName, user.getUserId())) {
			manufacturerService.save(manufacturer, user.getUserId());
		}else {
			model.addAttribute("duplicateEntry", "Duplicate Entry!");
		}
		
		model.addAttribute("manufacturerList", manufacturerService.listManufacturers(user.getUserId()));
		
		return "pages/manufacturerView";
	}	
	
//	@PostMapping("/manufacturer/delete")
//	public String manufacturerDelete(@RequestParam("formData")String manufacturerName, Model model, HttpSession session) {
//
//		User user = (User) session.getAttribute("user");
//
//		Manufacturers manufacturer = new Manufacturers();
//
//		manufacturer.setName(manufacturerName);
//
//		manufacturerService.delete(manufacturer, user.getUserId());
//
//		model.addAttribute("manufacturerList", manufacturerService.listManufacturers(user.getUserId()));
//
//		return "pages/manufacturerView";
//	}

}
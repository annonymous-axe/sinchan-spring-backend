package com.sinchan.controllers;

import java.util.List;

import com.sinchan.entities.Invoice;
import com.sinchan.entities.User;
import com.sinchan.services.InvoiceService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinchan.entities.Farmer;
import com.sinchan.entities.Tehsil;
import com.sinchan.services.FarmerService;
import com.sinchan.services.LocationServices;

import lombok.extern.slf4j.Slf4j;

@Controller
public class FarmerController {

	private final FarmerService farmerService;
	private final LocationServices locationService;
	private final InvoiceService invoiceService;
	
	public FarmerController(FarmerService farmerService, LocationServices locationService,
							@Qualifier("invoice") InvoiceService invoiceService) {
		this.farmerService = farmerService;
		this.locationService = locationService;
		this.invoiceService = invoiceService;
	}
	
	@GetMapping("farmerView")
	public String clientViewPage(Model model, HttpSession session) {

		User user = (User) session.getAttribute("user");

		List<Farmer> famerBeanList = farmerService.listFarmers(user.getUserId());
		
		model.addAttribute("famerBeanList", famerBeanList);
		
		return "pages/farmerView";
	}
	
	@GetMapping("createFarmer")
	public String createClient(Model model) {
		
		model.addAttribute("farmer", new Farmer());
		
		model.addAttribute("farmerID", "");
		
		model.addAttribute("districtList", locationService.districtList());
		
		return "pages/farmer";
	}
	
	@PostMapping(value = {"/farmerSave", "/farmerUpdate", "/farmerDelete"})
	public String progressFarmer(Farmer farmerBean, @ModelAttribute("btnAction") String btnAction, 
								Model model, HttpSession session) {

		User user = (User) session.getAttribute("user");
		
		if(btnAction.equalsIgnoreCase("create")) {
			farmerService.save(farmerBean, user.getUserId());
		}else if(btnAction.equalsIgnoreCase("update")) {
			farmerService.update(farmerBean, user.getUserId());
		}	
		else if(btnAction.equalsIgnoreCase("delete")) {
			farmerService.delete(farmerBean, user.getUserId());
		}
		
		return "redirect:/farmerView";
	}
	
	@PostMapping("getFarmer")
	public String getFarmer(@ModelAttribute("formData") int formData, Model model, HttpSession session) {

		User user = (User) session.getAttribute("user");

		Farmer farmer = farmerService.findById(formData, user.getUserId());

		List<Invoice> invoiceList = invoiceService.listInvoiceOfFarmers(formData, user.getUserId());

		model.addAttribute("doc_type", "invoice");

		model.addAttribute("invoiceList", invoiceList);
		
		model.addAttribute("farmer", farmer);
		
		model.addAttribute("farmerID", farmer.getId());
		
		model.addAttribute("districtList", locationService.districtList());
		
		if(farmer.getDistrict() > 0) {
			model.addAttribute("tehsilList", locationService.tehsilList(farmer.getDistrict()));
		}
		
		return "pages/farmer";
	}

}
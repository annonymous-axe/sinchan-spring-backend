package com.sinchan.controllers;

import com.sinchan.entities.Dictionary;
import com.sinchan.entities.User;
import com.sinchan.utility.LabelValService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.sinchan.entities.ItemManufacturerDetails;
import com.sinchan.entities.Items;
import com.sinchan.services.CategoryService;
import com.sinchan.services.ItemService;
import com.sinchan.services.ManufacturersService;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ItemController {

    private final ItemService itemService;
    private final CategoryService categoryService;
    private final ManufacturersService manufacturersService;
	private final LabelValService labelValService;

    public ItemController(ItemService itemService, CategoryService categoryService, ManufacturersService manufacturersService,
						  LabelValService labelValService){
        this.itemService = itemService;
        this.categoryService = categoryService;
        this.manufacturersService = manufacturersService;
		this.labelValService = labelValService;
    }

    @GetMapping("/item/view")
    public String itemView(Model model, HttpSession session){

		User user = (User) session.getAttribute("user");

        List<Items> itemList = itemService.listItems(user.getUserId());

        model.addAttribute("itemList", itemList);

        return "pages/itemView";
    }

    @GetMapping("/item/create")
    public String itemCreate(Model model, HttpSession session){

		User user = (User) session.getAttribute("user");

    	Items item = new Items();
    	
    	model.addAttribute("manufacturers", manufacturersService.listManufacturers(user.getUserId()));

        model.addAttribute("item", item);
        model.addAttribute("categoryList", categoryService.listCategory(user.getUserId()));


        return "pages/item";

    }
    
	@PostMapping(value = {"/item/save", "/item/update", "/item/delete"})
	public String progressItem(@ModelAttribute("item")Items item, @ModelAttribute("btnAction") String btnAction, HttpSession session) {

		User user = (User) session.getAttribute("user");

		if(btnAction.equalsIgnoreCase("create")) {
			itemService.save(item, user.getUserId());
		}else if(btnAction.equalsIgnoreCase("update")) {
			itemService.update(item, user.getUserId());
		}	
		else if(btnAction.equalsIgnoreCase("delete")) {
			itemService.delete(item.getId(), user.getUserId());
		}
		
		return "redirect:/item/view";
	}
	
	@PostMapping("/item/open")
	public String getgetItemarmer(@ModelAttribute("formData") int formData, Model model, HttpSession session) {

		User user = (User) session.getAttribute("user");
		
		Items item= itemService.findById(formData, user.getUserId());
		
		model.addAttribute("item", item);
		model.addAttribute("itemID", item.getId());
		model.addAttribute("manufacturers", manufacturersService.listManufacturers(user.getUserId()));
		model.addAttribute("categoryList", categoryService.listCategory(user.getUserId()));
		
		return "pages/item";
	}


	@ResponseBody
	@PostMapping("getItemDetails")
	public Dictionary getItemDetails(
			@RequestParam("itemId") int itemId,
			@RequestParam("manufacturerId") int manufacturerId, HttpSession session) {

		User user = (User) session.getAttribute("user");

		Dictionary objArr = labelValService.getItemDetails(itemId, manufacturerId, user.getUserId());

		return objArr;

	}

}
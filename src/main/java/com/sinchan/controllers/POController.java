package com.sinchan.controllers;

import com.sinchan.entities.*;
import com.sinchan.services.POService;
import com.sinchan.utility.LabelValService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class POController {

    private final POService poService;
    private final LabelValService lblValService;

    public POController(POService poService, LabelValService lblValService) {
        this.poService = poService;
        this.lblValService = lblValService;
    }

    @GetMapping("purchaseOrderView")
    public String purchaseOrderViewPage(Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");

        List<PurchaseOrder> poList = poService.listPurchaseOrders(user.getUserId());

        model.addAttribute("poList", poList);

        return "pages/purchaseOrderView";
    }

    @GetMapping("createPurchaseOrder")
    public String createPurchaseOrder(Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");

        model.addAttribute("purchaseOrder", new PurchaseOrder());

        model.addAttribute("categoryLblValList", lblValService.getCategoryLabelValList(user.getUserId()));

        model.addAttribute("manufacturerLblValList", lblValService.getManufacturerLabelValList(user.getUserId()));

        model.addAttribute("purchaseOrderID", "");

        return "pages/purchaseOrder";
    }

    @PostMapping(value = {"/purchaseOrderSave", "/purchaseOrderUpdate", "/purchaseOrderDelete"})
    public String progressPurchaseOrder(PurchaseOrder purchaseOrder, @ModelAttribute("btnAction") String btnAction, Model model,
                                        HttpSession session) {

        User user = (User) session.getAttribute("user");

        purchaseOrder.setCreatedAt(new Date());

        if(btnAction.equalsIgnoreCase("create")) {
            poService.save(purchaseOrder, user.getUserId());
        }else if(btnAction.equalsIgnoreCase("update")) {
            poService.update(purchaseOrder, user.getUserId());
        }
        else if(btnAction.equalsIgnoreCase("delete")) {
            poService.delete(purchaseOrder.getId(), user.getUserId());
        }

        return "redirect:/purchaseOrderView";
    }

    @PostMapping("getPurchaseOrder")
    public String getPurchaseOrder(@ModelAttribute("formData") int formData, Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");

        PurchaseOrder purchaseOrder = poService.findById(formData, user.getUserId());
        
        model.addAttribute("categoryLblValList", lblValService.getCategoryLabelValList(user.getUserId()));
        
        for(PurchaseOrderItems poItem : purchaseOrder.getPurchaseOrderItemsList()) {
        	if(poItem.getCategoryId()>0) {
        		poItem.setItemLblValList(lblValService.getItemFromCategoryLabelValItemList(poItem.getCategoryId(), user.getUserId()));
        	}
        }

        model.addAttribute("manufacturerLblValList", lblValService.getManufacturerLabelValList(user.getUserId()));

        model.addAttribute("purchaseOrder", purchaseOrder);
        model.addAttribute("purchaseOrderID", purchaseOrder.getId());

        return "pages/purchaseOrder";
    }
    
    @ResponseBody
    @PostMapping("getCategoriesFromItemId")
    public List<Dictionary> getCategoriesFromItemId(@RequestParam("itemName") String itemName, HttpSession session) {

        User user = (User) session.getAttribute("user");

        List<Dictionary> categoryList = lblValService.getCategoryLabelValListFromItemName(itemName, user.getUserId());
        
        return categoryList;
        
    }    

}
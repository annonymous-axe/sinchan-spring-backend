package com.sinchan.controllers;

import com.sinchan.entities.User;
import com.sinchan.services.InvoiceService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.sinchan.entities.Invoice;
import com.sinchan.entities.InvoiceItems;
import com.sinchan.services.LocationServices;
import com.sinchan.utility.LabelValService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class QuotationController {

	private final InvoiceService quotationService;
    private final LabelValService lblValService;
    private final LocationServices locationService;

    public QuotationController(@Qualifier("quotation") InvoiceService quotationService, LabelValService lblValService,
                               LocationServices locationService){
        this.quotationService = quotationService;
        this.lblValService = lblValService;
        this.locationService = locationService;
    }

    @GetMapping("/quotation/view")
    public String InvoiceView(Model model, HttpSession session){

        User user = (User) session.getAttribute("user");

        List<Invoice> invoiceList = quotationService.listInvoice(user.getUserId());

        model.addAttribute("doc_type", "quotation");

        model.addAttribute("invoiceList", invoiceList);

        return "pages/invoiceView";

    }
    
    @GetMapping("/quotation/create")
    public String createInvoice(Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");

    	Invoice invoice = new Invoice();
    	
    	invoice.setType(true);
    	
        model.addAttribute("invoice", invoice);

        model.addAttribute("manufacturerLblValList", lblValService.getManufacturerLabelValList(user.getUserId()));
        
        model.addAttribute("categoryLblValList", lblValService.getCategoryLabelValList(user.getUserId()));

        model.addAttribute("invoiceID", "");

        return "pages/invoice";
    }
    
    @PostMapping(value = {"/quotation/save", "/quotation/update", "/quotation/delete"})
    public String progressInvoice(Invoice invoice, @ModelAttribute("btnAction") String btnAction, Model model) {

        invoice.setCreatedAt(new Date());

//        if(btnAction.equalsIgnoreCase("create")) {
//        	InvoiceService.save(invoice);
//        }else if(btnAction.equalsIgnoreCase("update")) {
//        	InvoiceService.update(invoice);
//        }
//        else if(btnAction.equalsIgnoreCase("delete")) {
//        	InvoiceService.delete(invoice);
//        }

        return "redirect:/Invoice/view";
    }

    @PostMapping("/getQuotation")
    public String getQuotation(@ModelAttribute("formData") int formData, Model model,
                             HttpSession session) {

        User user = (User) session.getAttribute("user");

        Invoice invoice = quotationService.findById(formData, user.getUserId());

        invoice.setType(true);

        model.addAttribute("categoryLblValList", lblValService.getCategoryLabelValList(user.getUserId()));

        for(InvoiceItems invoiceItem : invoice.getInvoiceItemList()) {
            if(invoiceItem.getCategoryId()>0) {
                invoiceItem.setItemLblValList(lblValService.getItemFromCategoryLabelValItemList(invoiceItem.getCategoryId(), user.getUserId()));
            }
        }

        model.addAttribute("manufacturerLblValList", lblValService.getManufacturerLabelValList(user.getUserId()));

        model.addAttribute("districtList", locationService.districtList());

        if(invoice.getDistrict() > 0) {
            model.addAttribute("tehsilList", locationService.tehsilList(invoice.getDistrict()));
        }

        model.addAttribute("invoice", invoice);
        model.addAttribute("invoiceID", invoice.getId());
        model.addAttribute("doc_type", "quotation");

        return "pages/invoice";
    }

}

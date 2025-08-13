package com.sinchan.controllers;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.sinchan.entities.Dictionary;
import com.sinchan.entities.Farmer;
import com.sinchan.entities.Invoice;
import com.sinchan.entities.InvoiceItems;
import com.sinchan.entities.User;
import com.sinchan.pdf_generator_service.PDFGeneratorService;
import com.sinchan.services.FarmerService;
import com.sinchan.services.InvoiceService;
import com.sinchan.services.LocationServices;
import com.sinchan.services.UserService;
import com.sinchan.utility.EmailService;
import com.sinchan.utility.LabelValService;

@Controller
public class InvoiceController {

	private final InvoiceService invoiceService;
	private final LabelValService lblValService;
	private final FarmerService farmerService;
    private final PDFGeneratorService pdfGeneratorService;
    private final EmailService emailService;
    private final LocationServices locationService;
    private final UserService userService;
    private final InvoiceService quotationService;
	
    public InvoiceController(@Qualifier("invoice") InvoiceService invoiceService, LabelValService lblValService, FarmerService farmerService,
                             EmailService emailService, LocationServices locationService, PDFGeneratorService pdfGeneratorService,
                             UserService userService, @Qualifier("quotation") InvoiceService quotationService) {
        this.invoiceService = invoiceService;
        this.lblValService = lblValService;
        this.farmerService = farmerService;
        this.emailService = emailService;
        this.locationService = locationService;
        this.pdfGeneratorService = pdfGeneratorService;
        this.userService = userService;
        this.quotationService = quotationService;
    }

    @GetMapping("invoiceView")
    public String invoiceViewPage(Model model, HttpSession session) {
    	
    	User user = (User) session.getAttribute("user");

        List<Invoice> invoiceList = invoiceService.listInvoice(user.getUserId());

        model.addAttribute("doc_type", "invoice");

        model.addAttribute("invoiceList", invoiceList);

        return "pages/invoiceView";
    }

    @GetMapping("createInvoice")
    public String createInvoice(Model model, HttpSession session) {
    	
    	User user = (User) session.getAttribute("user");
    	
    	Invoice invoice = new Invoice();
    	
    	invoice.setType(false);

        model.addAttribute("invoice", invoice);

        model.addAttribute("manufacturerLblValList", lblValService.getManufacturerLabelValList(user.getUserId()));
        
        model.addAttribute("categoryLblValList", lblValService.getCategoryLabelValList(user.getUserId()));
        
		model.addAttribute("districtList", locationService.districtList());

        model.addAttribute("invoiceID", "");

        return "pages/invoice";
    }
    
    @PostMapping("createInvoice")
    public String createInvoiceWithDetails(@ModelAttribute("formData") int formData, Model model, HttpSession session) {
    	
    	Invoice invoice = new Invoice();

        User user = (User) session.getAttribute("user");

    	Farmer farmer = farmerService.findById(formData, user.getUserId());

    	invoice.setFarmer(formData);
        invoice.setFarmerName(farmer.getFarmerName());
    	invoice.setEmail(farmer.getEmail());
    	invoice.setContactNo(farmer.getContactNo());
    	invoice.setAddress(farmer.getAddress());
    	invoice.setSanch(farmer.getSanch());
    	invoice.setDistrict(farmer.getDistrict());
    	invoice.setTehsil(farmer.getTehsil());
    	invoice.setAadharId(farmer.getAadharId());
    	invoice.setFarmerId(farmer.getFarmerId());

        model.addAttribute("invoice", invoice);

        model.addAttribute("manufacturerLblValList", lblValService.getManufacturerLabelValList(user.getUserId()));
        
        model.addAttribute("categoryLblValList", lblValService.getCategoryLabelValList(user.getUserId()));
        
		model.addAttribute("districtList", locationService.districtList());
		
		if(farmer.getDistrict() > 0) {
			model.addAttribute("tehsilList", locationService.tehsilList(farmer.getDistrict()));
		}        

        model.addAttribute("invoiceID", "");

        return "pages/invoice";
    }    

    @PostMapping(value = {"/invoiceSave", "/invoiceUpdate", "/invoiceDelete"})
    public String progressInvoice(Invoice invoice, @ModelAttribute("btnAction") String btnAction,
    							  Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");
        
        if(btnAction.equalsIgnoreCase("create")) {
        	if(invoice.getType()) {
        		quotationService.save(invoice, user.getUserId());

                return "redirect:/quotation/view";
        	}else {
                invoiceService.save(invoice, user.getUserId());
            }
//        }else if(btnAction.equalsIgnoreCase("update")) {
//        	if(invoice.getType()) {
//        		quotationService.update(invoice, user.getUserId());
//        	}
//        	invoiceService.update(invoice, user.getUserId());
//        }
//        else if(btnAction.equalsIgnoreCase("delete")) {
//        	if(invoice.getType()) {
//        		quotationService.delete(invoice.getId(), user.getUserId())
//        	}
//        	invoiceService.delete(invoice.getId(), user.getUserId());
        }

        return "redirect:/invoiceView";
    }

    @PostMapping("/getInvoice")
    public String getInvoice(@ModelAttribute("formData") int formData, Model model,
    						HttpSession session) {
        
        User user = (User) session.getAttribute("user");

        Invoice invoice = invoiceService.findById(formData, user.getUserId());

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
        model.addAttribute("doc_type", "invoice");

        return "pages/invoice";
    }
    
    @ResponseBody
    @PostMapping("getItemsFromCategoryId")
    public List<Dictionary> getItemsFromCategoryId(@RequestParam("categoryId") int categoryId, HttpSession session){
        
        User user = (User) session.getAttribute("user");

        List<Dictionary> itemList = lblValService.getItemFromCategoryLabelValList(categoryId, user.getUserId());
        
        return itemList;    	
    	
    }

    @ResponseBody
    @PostMapping(path = {"/invoice/generate"})
    public void generateInvoicePDF(@RequestParam(name = "btnAction")int formData,
                                   @RequestParam(name = "doc_type")String doc_type,
    							   HttpServletResponse response, HttpSession session) throws IOException {
    	
    	User user = (User) session.getAttribute("user");
    	
    	Invoice invoice = null;

        if(doc_type.equalsIgnoreCase("invoice")){
            invoice = invoiceService.findById(formData, user.getUserId());
        }else if(doc_type.equalsIgnoreCase("quotation")){
            invoice = quotationService.findById(formData, user.getUserId());
        }

        if(invoice != null){
            byte[] byteArraySource = pdfGeneratorService.generateInvoice(invoice, user);
            //Set PDF response header
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition","attachment; filename=\"invoice-report.pdf\"");
            response.getOutputStream().write(byteArraySource);
            response.getOutputStream().flush();
        }

    }

    @ResponseBody
    @PostMapping(path = {"/send-email"})
    public ResponseEntity<String> sendEmail(@RequestParam(name = "itemId")int itemId, HttpSession session) throws IOException {

        User user = (User) session.getAttribute("user");
        
//        Invoice invoice = invoiceService.findById(itemId);
//
//        byte[] byteArraySource = pdfGeneratorService.generateInvoice(invoice, user);
//
//        emailService.sendMail(invoice.getEmail(), invoice.getType() ? "Quotation" : "Invoice",
//                "Dear " + invoice.getCustomerName() + ",\nPlease find attached your " + (invoice.getType() ? "quotation." : "invoice."),
//                byteArraySource
//        );

        return ResponseEntity.ok("Email sent successfully.");


    }
}
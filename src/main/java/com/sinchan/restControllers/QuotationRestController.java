package com.sinchan.restControllers;

import com.sinchan.entities.Invoice;
import com.sinchan.entities.User;
import com.sinchan.services.InvoiceService;
import com.sinchan.user.credentials.SinchanAuthToken;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuotationRestController {

    private final InvoiceService invoiceService;

    public QuotationRestController(@Qualifier("quotation") InvoiceService invoiceService){
        this.invoiceService = invoiceService;
    }

    @GetMapping("quotation/list")
    public List<Invoice> quotationList(){

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

        return invoiceService.listInvoice(user.getUserId());
    }

    @PostMapping("quotation")
    public void saveQuotation(@RequestBody Invoice invoice){

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

        invoiceService.save(invoice, user.getUserId());
    }

    @GetMapping("quotation")
    public Invoice openQuotation(@RequestParam int quotationId){

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

        return invoiceService.findById(quotationId, user.getUserId());
    }
}

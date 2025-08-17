package com.sinchan.restControllers;

import com.sinchan.entities.Invoice;
import com.sinchan.entities.User;
import com.sinchan.services.InvoiceService;
import com.sinchan.user.credentials.SinchanAuthToken;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class InvoiceRestController {

    private final InvoiceService invoiceService;

    public InvoiceRestController(@Qualifier("invoice") InvoiceService invoiceService){
        this.invoiceService = invoiceService;
    }

    @GetMapping("invoice/list")
    public List<Invoice> invoiceList(){

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

        return invoiceService.listInvoice(user.getUserId());
    }

    @PostMapping("invoice")
    public void saveInvoice(@RequestBody Invoice invoice){

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

        invoiceService.save(invoice, user.getUserId());
    }

    @GetMapping("invoice")
    public Invoice openInvoice(@RequestParam int invoiceId){

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

        return invoiceService.findById(invoiceId, user.getUserId());
    }

}

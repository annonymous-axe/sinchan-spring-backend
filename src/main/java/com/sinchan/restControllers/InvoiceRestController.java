package com.sinchan.restControllers;

import com.sinchan.entities.Invoice;
import com.sinchan.services.InvoiceService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class InvoiceRestController {

    private final InvoiceService invoiceService;

    public InvoiceRestController(@Qualifier("invoice") InvoiceService invoiceService){
        this.invoiceService = invoiceService;
    }

    @GetMapping("invoice/list")
    public List<Invoice> invoiceList(){

        return invoiceService.listInvoice(100);
    }

    @PostMapping("invoice")
    public void saveInvoice(@RequestBody Invoice invoice){

        invoiceService.save(invoice, 100);
    }

    @GetMapping("invoice")
    public Invoice openInvoice(@RequestParam int invoiceId){

        return invoiceService.findById(invoiceId, 100);
    }
}

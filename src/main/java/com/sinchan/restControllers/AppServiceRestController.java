package com.sinchan.restControllers;

import com.sinchan.dao.UserDAO;
import com.sinchan.entities.District;
import com.sinchan.entities.Invoice;
import com.sinchan.entities.Tehsil;
import com.sinchan.entities.User;
import com.sinchan.pdf_generator_service.PDFGeneratorService;
import com.sinchan.services.InvoiceService;
import com.sinchan.services.LocationServices;
import com.sinchan.services.UserService;
import com.sinchan.user.credentials.SinchanAuthToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@RestController
public class AppServiceRestController {

    private final LocationServices locationService;
    private final InvoiceService invoiceService;
    private final InvoiceService quotationService;
    private final PDFGeneratorService pdfGeneratorService;
    private final UserService userService;

    public AppServiceRestController(LocationServices locationServices,
                                    @Qualifier("invoice") InvoiceService invoiceService,
                                    @Qualifier("quotation") InvoiceService quotationService,
                                    PDFGeneratorService pdfGeneratorService,
                                    UserService userService){
        this.locationService = locationServices;
        this.invoiceService = invoiceService;
        this.quotationService = quotationService;
        this.pdfGeneratorService = pdfGeneratorService;
        this.userService = userService;
    }

    @GetMapping("district/list")
    public List<District> districtList(HttpServletRequest request) {

        Locale locale = RequestContextUtils.getLocale(request);

        return locationService.districtList();
    }

    @PostMapping("tehsil/list")
    public List<Tehsil> tehsilList(@RequestParam("districtId") int districtId) {

        return locationService.tehsilList(districtId);
    }

    @PostMapping(path = {"/invoice/generate"})
    public void generateInvoicePDF(@RequestParam int invoiceId,
                                   @RequestParam(name = "doc_type")String doc_type,
                                   HttpServletResponse response) throws IOException {

        SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

        User user = userService.findUserByEmail(authToken.getUser().getEmail());

        Invoice invoice = null;

        if(doc_type.equalsIgnoreCase("invoice")){
            invoice = invoiceService.findById(invoiceId, user.getUserId());
        }else if(doc_type.equalsIgnoreCase("quotation")){
            invoice = quotationService.findById(invoiceId, user.getUserId());
        }

        UserDAO userDAO = userService.loadUserDAOByUsername(user.getEmail());
        if(invoice != null){
            byte[] byteArraySource = pdfGeneratorService.generateInvoice(invoice, userDAO);
            //Set PDF response header
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition","attachment; filename=\"invoice-report.pdf\"");
            response.getOutputStream().write(byteArraySource);
            response.getOutputStream().flush();
        }

    }

    @PostMapping(path = {"/send-email"})
    public ResponseEntity<String> sendEmail(@RequestParam(name = "itemId")int itemId) throws IOException {

        SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

        User user = authToken.getUser();

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

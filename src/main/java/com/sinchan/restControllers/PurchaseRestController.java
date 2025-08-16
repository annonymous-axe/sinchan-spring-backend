package com.sinchan.restControllers;

import com.sinchan.entities.Invoice;
import com.sinchan.entities.PurchaseOrder;
import com.sinchan.entities.User;
import com.sinchan.services.InvoiceService;
import com.sinchan.services.POService;
import com.sinchan.user.credentials.SinchanAuthToken;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PurchaseRestController {

    private final POService poService;

    public PurchaseRestController(POService poService){
        this.poService = poService;
    }

    @GetMapping("purchase/list")
    public List<PurchaseOrder> purchaseList(){

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

        return poService.listPurchaseOrders(user.getUserId());
    }

    @PostMapping("purchase")
    public void savePurchase(@RequestBody PurchaseOrder purchaseOrder){

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

        poService.save(purchaseOrder, user.getUserId());
    }

    @GetMapping("purchase")
    public PurchaseOrder openPurchase(@RequestParam int purchaseOrderId){

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

        return poService.findById(purchaseOrderId, user.getUserId());
    }
}

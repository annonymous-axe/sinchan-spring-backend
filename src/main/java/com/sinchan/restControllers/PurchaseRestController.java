package com.sinchan.restControllers;

import com.sinchan.entities.Invoice;
import com.sinchan.entities.PurchaseOrder;
import com.sinchan.entities.PurchaseOrderItems;
import com.sinchan.entities.User;
import com.sinchan.services.InvoiceService;
import com.sinchan.services.POService;
import com.sinchan.user.credentials.SinchanAuthToken;
import com.sinchan.utility.LabelValService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PurchaseRestController {

    private final POService poService;
    private final LabelValService lblValService;

    public PurchaseRestController(POService poService, LabelValService lblValService){
        this.poService = poService;
        this.lblValService = lblValService;
    }

    @GetMapping("purchase/list")
    public List<PurchaseOrder> purchaseList(){

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

        return poService.listPurchaseOrders(user.getUserId());
    }

    @PostMapping("purchase")
    public void savePurchase(@RequestBody PurchaseOrder purchaseOrder){

        System.out.println("po number : "+purchaseOrder.getPoNumber());
        System.out.println("supplierName :" +purchaseOrder.getSupplierNameEn());
        System.out.println("purchase date : "+purchaseOrder.getPurchaseDate());
        System.out.println("bill number: "+purchaseOrder.getBillNumber());

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

        poService.save(purchaseOrder, user.getUserId());
    }

    @GetMapping("purchase")
    public PurchaseOrder openPurchase(@RequestParam int purchaseOrderId){

		SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

		User user = authToken.getUser();

        PurchaseOrder purchaseOrder = poService.findById(purchaseOrderId, user.getUserId());
        for(PurchaseOrderItems item: purchaseOrder.getPurchaseOrderItemsList()){
            item.setItemLblValList(lblValService.getItemFromCategoryLabelValItemList(item.getCategoryId(), user.getUserId()));
        }

        return purchaseOrder;
    }
}

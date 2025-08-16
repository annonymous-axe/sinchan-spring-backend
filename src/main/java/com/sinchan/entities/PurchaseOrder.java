package com.sinchan.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class PurchaseOrder {

    private int id;

    private String poNumber;

    private String supplierNameEn;

    private String supplierNameMh;

    private Date purchaseDate;

    private String billNumber;

    private Date createdAt;

    private List<PurchaseOrderItems> purchaseOrderItemsList = new ArrayList<>();

}

package com.sinchan.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PurchaseOrderItems {

    private int id;

    private int itemId;

    private int manufacturerId;

    private int quantity;

    private float rate;

    private int categoryId;

    private List<Category> categoryList = new ArrayList<>();

    private String cmlNumber;

    private String totalAmount;

	private List<Dictionary> itemLblValList = new ArrayList<>();

    private int purchaseOrderId;

}

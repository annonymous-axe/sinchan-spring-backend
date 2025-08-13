package com.sinchan.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class InvoiceItems {
	
	private int id;
	
	private int categoryId;

	private String categoryName;
	
	private int itemId;

	private String itemName;

	private int quantity;
	
	private String unit;
	
	private float rate;
	
	private float total;
	
	private String avlQuantity;
	
	private String cmlNumber;
	
	private List<Dictionary> itemLblValList = new ArrayList<>();
	
	private int invoiceId;

	private int quotationId; 

}
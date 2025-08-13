package com.sinchan.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Invoice {
	
    private int id;

    private int farmer;

    private String farmerName;

    private String email;

    private String contactNo;

    private String address;

    private Date createdAt;

    private int manufacturerId;
    
    private String manufacturerName;
    
    private float grandTotal;

    private boolean type;
    
	private String sanch;
	
	private int district;
	
	private int tehsil;

    private String districtName;

    private String tehsilName;
	
	private String aadharId;
	
	private String farmerId;

    private List<InvoiceItems> invoiceItemList = new ArrayList<>();

    public boolean getType(){
        return this.type;
    }

    public void setType(boolean type){
        this.type = type;
    }

}
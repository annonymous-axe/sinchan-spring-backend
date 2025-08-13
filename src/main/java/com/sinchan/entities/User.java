package com.sinchan.entities;

import lombok.Data;

@Data
public class User {
	
	private int userId;
	
	private String email;
	
	private String password;
	
	private String firstName;
	
	private String lastName;
	
	private String contactNumber;
	
	private String firmName;
	
	private String gstNumber;
	
	private boolean active;
	
	private String address;

	public boolean getActive(){
		return this.active;
	}

	public void setActive(boolean active){
		this.active = active;
    }

}

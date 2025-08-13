package com.sinchan.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
//@Entity
//@Table(name = "farmers")
public class Farmer{

	private int id;
	
	private String farmerName;
	
	private String email;
	
	private String contactNo;
	
	private String address;

	private String sanch;

	private int district;

	private int tehsil;
	
	private String aadharId;
	
	private String farmerId;

	@JsonIgnore
	private List<Tehsil> tehsilList = new ArrayList<>();
	
}
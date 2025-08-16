package com.sinchan.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Farmer{

	private int id;
	
	private String farmerNameEn;

	private String farmerNameMh;
	
	private String email;
	
	private String contactNo;
	
	private String addressEn;

	private String addressMh;

	private String sanch;

	private int district;

	private int tehsil;
	
	private String aadharId;
	
	private String farmerId;

	@JsonIgnore
	private List<Tehsil> tehsilList = new ArrayList<>();
	
}
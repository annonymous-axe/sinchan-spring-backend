package com.sinchan.services;

import java.util.List;

import com.sinchan.entities.District;
import com.sinchan.entities.Tehsil;

public interface LocationServices {
	
	List<District> districtList();
	
	List<Tehsil> tehsilList(int districtId);

}

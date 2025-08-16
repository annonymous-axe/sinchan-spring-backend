package com.sinchan.services;

import java.util.List;

import com.sinchan.entities.Dictionary;
import com.sinchan.entities.Farmer;

public interface FarmerService {
	
	void save(Farmer farmer, int userId);
	
	List<Farmer> listFarmers(int userId);
	
	Farmer findById(int id, int userId);
	
	void update(Farmer farmer, int userId);
	
	void delete(Farmer farmerBean, int userId);

	// for rest apis

	void delete(int farmerId, int userId);

	List<Dictionary> listSanch(int userId);

}

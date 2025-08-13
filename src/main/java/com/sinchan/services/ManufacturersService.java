package com.sinchan.services;

import java.util.List;

import com.sinchan.entities.Manufacturers;

public interface ManufacturersService {
	
	List<Manufacturers> listManufacturers(int userId);
	
	void save(Manufacturers manufacturer, int userId);
	
	boolean exist(String manufacturerName, int userId);

	void delete(int manufacturerId, int userId);

	Manufacturers findById(int manufacturerId, int userId);

	void update(Manufacturers manufacturer, int userId);

}

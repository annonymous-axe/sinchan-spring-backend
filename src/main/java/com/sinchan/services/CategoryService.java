package com.sinchan.services;

import java.util.List;

import com.sinchan.entities.Category;

public interface CategoryService {
	
	List<Category> listCategory(int userId, String locale);

}

package com.sinchan.services;

import com.sinchan.entities.User;

public interface UserService {
	
	User findUserByEmail(String email);
	
	void update(User user);

	void delete(User user);

}

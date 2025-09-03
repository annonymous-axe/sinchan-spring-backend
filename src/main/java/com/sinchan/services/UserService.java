package com.sinchan.services;

import com.sinchan.dao.UserDAO;
import com.sinchan.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService{
	
	User findUserByEmail(String email);
	
	void update(UserDAO user, int userId);

	void delete(UserDAO user, int userId);

	UserDAO loadUserDAOByUsername(String email);

}

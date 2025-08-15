package com.sinchan.services;

import com.sinchan.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService{
	
	User findUserByEmail(String email);
	
	void update(User user);

	void delete(User user);

}

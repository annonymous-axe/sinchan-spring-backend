package com.sinchan.entities;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class User implements UserDetails {
	
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

	private List<SimpleGrantedAuthority> role;

	public boolean getActive(){
		return this.active;
	}

	public void setActive(boolean active){
		this.active = active;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return role;
	}

	@Override
	public String getUsername() {
		return email;
	}
}

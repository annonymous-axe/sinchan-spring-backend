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
	
	private String fullNameEn;

	private String fullNameMh;
	
	private String contactNumber;
	
	private String firmNameEn;

	private String firmNameMh;
	
	private String gstNumber;

	private boolean active;
	
	private String addressEn;

	private String addressMh;

	private String imageStr;

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
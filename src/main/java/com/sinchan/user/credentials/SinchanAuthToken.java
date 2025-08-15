package com.sinchan.user.credentials;

import com.sinchan.entities.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

public class SinchanAuthToken extends UsernamePasswordAuthenticationToken {

    private final User user;

    public SinchanAuthToken(Object principal, Object credentials, List<SimpleGrantedAuthority> roles, User user) {
        super(principal, credentials, roles);
        this.user = user;
    }

    public User getUser(){
        return user;
    }
}

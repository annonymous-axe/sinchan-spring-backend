package com.sinchan.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.sinchan.entities.User;
import com.sinchan.security.JWTUtility;
import com.sinchan.services.UserService;
import com.sinchan.user.credentials.SinchanAuthToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Run this filter on every request
@Component
public class JWTFilter extends OncePerRequestFilter {

    private UserService userService;

    private JWTUtility jwtUtility;

    public JWTFilter(UserService userService, JWTUtility jwtUtility){
        this.userService = userService;
        this.jwtUtility = jwtUtility;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if(authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer")) {

            token = authHeader.substring(7);

            username = jwtUtility.validateToken(token);

        }

        if(username != null && !username.isBlank()){

            User userDetails = (User) userService.loadUserByUsername(username);

            SinchanAuthToken authToken = new SinchanAuthToken(userDetails.getEmail(), userDetails.getPassword(), userDetails.getRole(), userDetails);

            if(SecurityContextHolder.getContext().getAuthentication() == null){
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }

        filterChain.doFilter(request, response);

    }
}

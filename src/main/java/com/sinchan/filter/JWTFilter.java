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

        System.out.println("Entering in filter.");

//        System.out.println(""+request.get);

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        System.out.println("header : "+authHeader);

        if(authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer")) {

            token = authHeader.substring(7);

            username = jwtUtility.validateToken(token);

            System.out.println("token : " + token);
        }

        if(username != null && !username.isBlank()){

//            try{

            User userDetails = (User) userService.loadUserByUsername(username);

            SinchanAuthToken authToken = new SinchanAuthToken(userDetails.getEmail(), userDetails.getPassword(), userDetails.getRole(), userDetails);

            if(SecurityContextHolder.getContext().getAuthentication() == null){
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

//            }catch (JWTVerificationException exc){
//                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Token");
//            }
        }

        filterChain.doFilter(request, response);

        System.out.println("Exiting filter.");



    }
}

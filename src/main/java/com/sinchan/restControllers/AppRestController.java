package com.sinchan.restControllers;

import com.sinchan.dao.UserDAO;
import com.sinchan.security.JWTUtility;
import com.sinchan.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
public class AppRestController {

    private final JWTUtility jwtUtility;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AppRestController(JWTUtility jwtUtility, AuthenticationManager authenticationManager, UserService userService){
        this.jwtUtility = jwtUtility;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @GetMapping("login")
    public ResponseEntity<Object[]> login(@RequestParam("email") String email, @RequestParam("password") String password){

        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(email, password);

            authenticationManager.authenticate(authToken);

            String token = jwtUtility.generateToken(email);

            UserDAO userDAO = userService.loadUserDAOByUsername(email);

            System.out.println("token : "+token);
            return new ResponseEntity<>(new Object[]{token, userDAO}, HttpStatus.OK);
        }catch (AuthenticationException excec){
            System.out.println("exception : "+excec);
            return new ResponseEntity<>(new Object[]{"Invalid username/password."}, HttpStatus.UNAUTHORIZED);
        }
    }
}

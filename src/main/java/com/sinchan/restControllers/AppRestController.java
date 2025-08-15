package com.sinchan.restControllers;

import com.sinchan.security.JWTUtility;
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

    public AppRestController(JWTUtility jwtUtility, AuthenticationManager authenticationManager){
        this.jwtUtility = jwtUtility;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("login")
    public ResponseEntity<String> login(@RequestParam("email") String email, @RequestParam("password") String password){

        System.out.println("controller param email : "+email);
        System.out.println("controller param password : "+password);

        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(email, password);

            authenticationManager.authenticate(authToken);

            String token = jwtUtility.generateToken(email);

            System.out.println("token : "+token);
            return new ResponseEntity<>(token, HttpStatus.OK);
        }catch (AuthenticationException excec){
            System.out.println("exception : "+excec);
            return new ResponseEntity<>("Invalid username/password.", HttpStatus.UNAUTHORIZED);
        }
    }
}

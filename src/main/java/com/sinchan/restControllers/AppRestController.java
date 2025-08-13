package com.sinchan.restControllers;

import com.sinchan.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class AppRestController {

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody User user){

        System.out.println("username : "+user.getEmail());
        System.out.println("password : "+user.getPassword());

        return new ResponseEntity<>("Data receivied!", HttpStatus.OK);
    }
}

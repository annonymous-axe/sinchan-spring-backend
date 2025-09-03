package com.sinchan.restControllers;

import com.sinchan.dao.UserDAO;
import com.sinchan.entities.User;
import com.sinchan.services.UserService;
import com.sinchan.user.credentials.SinchanAuthToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@RestController
public class UserRestController {

    @Value("${app.upload.dir}")
    private String uploadDir;

    private final UserService userService;

    public UserRestController(UserService userService){
        this.userService = userService;
    }

    @PostMapping(value = "user", consumes = "multipart/form-data")
    public ResponseEntity<String> saveUser(@ModelAttribute UserDAO userDao) {


        try {
            // saved image into created directory
            File savedFile = new File(uploadDir + userDao.getImage().getOriginalFilename());
            userDao.getImage().transferTo(savedFile);

            System.out.println("Image saved success at location : "+savedFile.getAbsolutePath());
        }catch (Exception e){
            throw new RuntimeException("During image saving : "+e);
        }

        User user = userService.findUserByEmail(userDao.getEmail());

        userService.update(userDao, user.getUserId());

        return new ResponseEntity<>("User updated!", HttpStatus.OK);
    }

    @GetMapping("user")
    public UserDAO getUser(){

        SinchanAuthToken authToken = (SinchanAuthToken) SecurityContextHolder.getContext().getAuthentication();

        System.out.println("getting user for : "+authToken.getName());
        UserDAO userDAO = userService.loadUserDAOByUsername(authToken.getName());

        return userDAO;
    }
}
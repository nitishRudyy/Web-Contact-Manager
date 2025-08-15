package com.scm.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.UserService;

@ControllerAdvice
public class Rootcontroller {

 private Logger logger = LoggerFactory.getLogger(Rootcontroller.class);

    @Autowired
    private UserService userService;
    
    @ModelAttribute
    public void addLoggedUserToModel(Model model , Authentication authentication) {
        if (authentication == null) {
            return;
        }
        System.out.println("Adding logged in user information to model");

        String username = Helper.getEmailOfLoggedInUser(authentication);                                               
        logger.info("User logged: {} " , username);
        //database se data fetch: get user from db: email,name,address          
        User user = userService.getUserByEmail(username);
        System.out.println(user);
        System.out.println(user.getName() );
        System.out.println(user.getEmail() );
        model.addAttribute("loggedUser", user); // add user to model
    }
}

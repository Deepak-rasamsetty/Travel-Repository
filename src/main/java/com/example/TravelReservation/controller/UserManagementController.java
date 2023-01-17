package com.example.TravelReservation.controller;

import com.example.TravelReservation.payload.*;
import com.example.TravelReservation.service.UserManagementService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserManagementController {

    private static Logger LOGGER = LogManager.getLogger(UserManagementController.class);

    @Autowired
    UserManagementService userManagementService;

    @PostMapping("/")
    public UserCreationResponse createUser(@RequestBody UserCreateRequest request){
        LOGGER.info("Entered createUser, request - {}", request);
        UserCreationResponse response=userManagementService.createUser(request);
        LOGGER.info("leaving  createUser, response - {}", response);
        return response;

    }

    @PostMapping("/authenticate")
    public Boolean authenticateUser(@RequestBody AuthenticateUserRequest request){
        LOGGER.info("Entered authenticateUser, request - {}", request);
        Boolean response=userManagementService.authenticateUser(request);
        LOGGER.info("leaving  authenticateUser, response - {}", response);
        return response;

    }
    @PostMapping("/details")
    public UserDetailsResponse getUserDetails(@RequestBody UserDetailsRequest request){
        LOGGER.info("Entered createUser, request - {}", request);
        UserDetailsResponse response=userManagementService.getUserDetails(request);
        LOGGER.info("leaving  createUser, response - {}", response);
        return response;

    }
}

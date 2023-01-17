package com.example.TravelReservation.service;

import com.example.TravelReservation.controller.UserManagementController;
import com.example.TravelReservation.entity.UserDetails;
import com.example.TravelReservation.exceptions.UserCreationFailedException;
import com.example.TravelReservation.payload.*;
import com.example.TravelReservation.repository.UserDetailsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class UserManagementService {

    private static Logger LOGGER = LogManager.getLogger(UserManagementService.class);

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Autowired
    ModelMapper modelMapper;

    public UserCreationResponse createUser(UserCreateRequest request) {
        LOGGER.info("Entered createUser, request - {}", request);
        UserCreationResponse response = new UserCreationResponse();
        UserDetails newUser = modelMapper.map(request,UserDetails.class);
        try{
            userDetailsRepository.save(newUser);
        }catch(DataAccessException e){
            response.setStatus("FAILURE");
            response.setMessage("User creation failed. Please try later");
        }
        response.setStatus("SUCCESS");
        response.setMessage("User creation Success. Please log in once");

        LOGGER.info("Leaving createUser, response - {}", response);
        return response;
    }

    public Boolean authenticateUser(AuthenticateUserRequest request) {
        LOGGER.info("Entered authenticateUser, request - {}", request);
        Boolean response = userDetailsRepository.existsByEmailAndPassword(request.getUserName(), request.getPassword());
         LOGGER.info("Leaving authenticateUser, response - {}", response);
        return response;
    }

    public UserDetailsResponse getUserDetails(UserDetailsRequest request) {
        LOGGER.info("Entered getUserDetails, request - {}", request);
        UserDetails userDetails = userDetailsRepository.findByEmail(request.getEmail());
        UserDetailsResponse response = modelMapper.map(userDetails, UserDetailsResponse.class);
        LOGGER.info("Leaving getUserDetails, response - {}", response);
        return response;
    }
}

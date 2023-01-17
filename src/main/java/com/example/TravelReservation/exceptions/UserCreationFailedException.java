package com.example.TravelReservation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserCreationFailedException extends Exception {
    private  static final  String message = "User creation failed. Please try later";
    public UserCreationFailedException(){
        super(message);
    }
}


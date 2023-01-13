package com.example.TravelReservation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LocationIsInvalidException extends Exception{
    private  static final  String message = "Locations inside the provided routes are invalis. Please provide valid Location routes";
    public LocationIsInvalidException(){
        super(message);
    }
}

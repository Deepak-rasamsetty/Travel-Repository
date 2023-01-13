package com.example.TravelReservation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ServiceAlreadyPresentException extends Exception{
    private  static final  String message = "Bus Service with the given Service Id is already present. Please provide a different service id";
    public ServiceAlreadyPresentException(){
        super(message);
    }
}

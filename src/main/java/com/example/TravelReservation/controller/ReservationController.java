package com.example.TravelReservation.controller;


import com.example.TravelReservation.payload.ReserveTicketRequest;
import com.example.TravelReservation.payload.ReservationDetailsResponse;
import com.example.TravelReservation.service.ReservationService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReservationController {
    private static Logger LOGGER = LogManager.getLogger(ReservationController.class);
    @Autowired
    ReservationService reservationService;

    @PostMapping("/reserve")
    public Integer reserveTicket(@RequestBody ReserveTicketRequest request){
        LOGGER.info("Entered reserveTicket, request - {}", request);
        Integer reservationId = reservationService.reserveTickets(request);
        LOGGER.info("Leaving reserveTicket, reservationId - {}", reservationId);
        return reservationId;
    }

    @GetMapping("/getreservedseats/{serviceId}")
    public List<String> getReservedSeats(@PathVariable("serviceId") Integer serviceId){
        LOGGER.info("Entered getReservedSeats, serviceId - {}", serviceId);
        List<String> reservedSeats = reservationService.getReservedSeats(serviceId);
        LOGGER.info("Leaving getReservedSeats, reservedSeats - {}", reservedSeats);
        return reservedSeats;
    }
    @GetMapping("/getreservationdetails/{reservationId}")
    public ReservationDetailsResponse getReservationDetails(@PathVariable("reservationId") Integer reservationId){
        LOGGER.info("Entered getReservationDetails, reservationId - {}", reservationId);
        ReservationDetailsResponse transactionDetails = reservationService.getReservationDetails(reservationId);
        LOGGER.info("Leaving getReservationDetails, transactionDetails - {}", transactionDetails);
        return transactionDetails;
    }


}

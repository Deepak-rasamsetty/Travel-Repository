package com.example.TravelReservation.controller;


import com.example.TravelReservation.exceptions.TicketAlreadyReservedException;
import com.example.TravelReservation.payload.ReservationQueueResponse;
import com.example.TravelReservation.payload.ReserveTicketRequest;
import com.example.TravelReservation.payload.ReservationDetailsResponse;
import com.example.TravelReservation.service.ReservationService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class ReservationController {
    private static Logger LOGGER = LogManager.getLogger(ReservationController.class);
    @Autowired
    ReservationService reservationService;

    @PostMapping("/reserve")
    public Integer reserveTicket(@RequestBody ReserveTicketRequest request) throws TicketAlreadyReservedException {
        LOGGER.info("Entered reserveTicket, request - {}", request);
        Integer reservationId = reservationService.reserveTickets(request);
        LOGGER.info("Leaving reserveTicket, reservationId - {}", reservationId);
        return reservationId;
    }

    @PostMapping("/reserve/async")
    public Integer reserveTicketAsync(@RequestBody ReserveTicketRequest request) throws TicketAlreadyReservedException {
        LOGGER.info("Entered reserveTicketAsync, request - {}", request);
        Integer reservationQueueId = reservationService.addToReservationQueue();
        CompletableFuture.runAsync(()->{
            reservationService.reserveTicketAsync(request, reservationQueueId);
        });
       LOGGER.info("Leaving reserveTicketAsync, reservationQueueId - {}", reservationQueueId);
        return reservationQueueId;
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

    @GetMapping("/checkReservationQueueStatus/{reservationQueueId}")
    public ReservationQueueResponse getReservationQueueStatus(@PathVariable("reservationQueueId") Integer reservationQueueId){
        LOGGER.info("Entered getReservationDetails, reservationQueueId - {}", reservationQueueId);
        ReservationQueueResponse reservationQueueResponse = reservationService.getReservationQueueStatus(reservationQueueId);
        LOGGER.info("Leaving getReservationDetails, reservationQueueResponse - {}", reservationQueueResponse);
        return reservationQueueResponse;
    }


}

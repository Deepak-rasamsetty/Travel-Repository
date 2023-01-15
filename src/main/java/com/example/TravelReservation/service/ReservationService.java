package com.example.TravelReservation.service;

import com.example.TravelReservation.entity.ReservationDetails;
import com.example.TravelReservation.entity.ReservationMap;
import com.example.TravelReservation.entity.TicketDetails;
import com.example.TravelReservation.payload.*;
import com.example.TravelReservation.repository.ReservationDetailsRepository;
import com.example.TravelReservation.repository.ReservationMapRepository;
import com.example.TravelReservation.repository.TicketDetailsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class ReservationService {

    private static Logger LOGGER = LogManager.getLogger(ReservationService.class);
    @Autowired
    TicketDetailsRepository ticketDetailsRepository;
    @Autowired
    ReservationDetailsRepository reservationDetailsRepository;
    @Autowired
    ReservationMapRepository reservationMapRepository;
    @Autowired
    DetailsService detailsService;

    @Autowired
    RoutesService routesService;

    @Autowired
    ModelMapper modelMapper;
    public Integer reserveTickets(ReserveTicketRequest request) {
        LOGGER.info("Enetered reserveTickets, request - {}", request);
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ReservationDetails reservation = createReservation(request);
        Consumer<String> persistTicketsConsumer = (seatNo)->{
            TicketDetails ticket = new TicketDetails();
            ticket.setServiceId(request.getServiceId());
            ticket.setSeatNumber(seatNo);
            TicketDetails createdTicket = ticketDetailsRepository.save(ticket);

            ReservationMap reservationMap = new ReservationMap();
            reservationMap.setReservationId(reservation.getReservationId());
            reservationMap.setTicketId(createdTicket.getTicketId());

            reservationMapRepository.save(reservationMap);
        };
        request.getSeatNumbers().stream().forEach(persistTicketsConsumer);

        LOGGER.info("Leaving reserveTickets, reservation - {}", reservation);
        return reservation.getReservationId();
    }

    public List<String> getReservedSeats(Integer serviceId) {
        LOGGER.info("Enetered getReservedSeats, serviceId - {}", serviceId);
        List<String> reservedSeats = ticketDetailsRepository.getReservedSeatNumbersByServiceId(serviceId);
        LOGGER.info("Leaving getReservedSeats, reservedSeats - {}", reservedSeats);
        return reservedSeats;
    }

    public ReservationDetails createReservation(ReserveTicketRequest request){
        LOGGER.info("Enetered createReservation, request - {}", request);
        ReservationDetails reservation = new ReservationDetails();
        reservation.setCreatedOn(new Date().toString());
        reservation.setServiceId(request.getServiceId());
        reservation.setBoardingLocation(request.getBoardingLocation());
        reservation.setDroppingLocation(request.getDroppingLocation());
        ReservationDetails createdReservation = reservationDetailsRepository.save(reservation);
        LOGGER.info("Leaving createReservation, createdReservation - {}", createdReservation);
        return createdReservation;
    }

    public ReservationDetailsResponse getReservationDetails(Integer reservationId) {
        LOGGER.info("Enetered getReservationDetails, reservationId - {}", reservationId);
        Optional<ReservationDetails> reservation = reservationDetailsRepository.findById(reservationId);
        BusInfo busInfo = detailsService.getBusDetails(reservation.get().getServiceId());
        JourneyDetails journeyDetails = routesService.getJourneyDetails(reservation.get().getServiceId(),
                reservation.get().getBoardingLocation(), reservation.get().getDroppingLocation());
        busInfo.setJourneyDetails(journeyDetails);
        List<TicketDetailsResponse> ticketDetails = new ArrayList<>();
        List<ReservationMap> reservationMapList = reservationMapRepository.findByReservationId(reservationId);
        Consumer<ReservationMap> addToTicketDetailsConsumer = (reservationMap)->{
            Optional<TicketDetails> ticket = ticketDetailsRepository.findById(reservationMap.getTicketId());
            TicketDetailsResponse ticketResp = modelMapper.map(ticket.get(), TicketDetailsResponse.class);
            ticketDetails.add(ticketResp);
        };
        reservationMapList.stream().forEach(addToTicketDetailsConsumer);

        ReservationDetailsResponse response = new ReservationDetailsResponse();
        response.setReservationId(reservationId);
        response.setBusInfo(busInfo);
        response.setTicketDetails(ticketDetails);
        response.setCreatedOn(response.getCreatedOn());
        LOGGER.info("Leaving getReservationDetails, response - {}", response);
        return response;
    }

}

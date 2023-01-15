package com.example.TravelReservation.service;

import com.example.TravelReservation.entity.ReservationDetails;
import com.example.TravelReservation.entity.ReservationMap;
import com.example.TravelReservation.entity.ReservationQueue;
import com.example.TravelReservation.entity.TicketDetails;
import com.example.TravelReservation.exceptions.TicketAlreadyReservedException;
import com.example.TravelReservation.payload.*;
import com.example.TravelReservation.repository.ReservationDetailsRepository;
import com.example.TravelReservation.repository.ReservationMapRepository;
import com.example.TravelReservation.repository.ReservationQueueRepository;
import com.example.TravelReservation.repository.TicketDetailsRepository;
import com.example.TravelReservation.utility.CustomUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    CustomUtilities customUtilities;

    @Autowired
    ReservationQueueRepository reservationQueueRepository;

    @Autowired
    ModelMapper modelMapper;

    @Transactional
    public Integer reserveTickets(ReserveTicketRequest request) throws TicketAlreadyReservedException {
        LOGGER.info("Enetered reserveTickets, request - {}", request);
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(checkIfRequestSeatsAreAlreadyReserved(request.getServiceId(), request.getSeatNumbers())){
            throw new TicketAlreadyReservedException();
        }
        ReservationDetails reservation = createReservation(request);
        Consumer<String> persistTicketsConsumer = (seatNo)->{
            createNewTicketAndAddToReservation(request.getServiceId(), reservation.getReservationId(), seatNo);
        };
        request.getSeatNumbers().stream().forEach(persistTicketsConsumer);
        LOGGER.info("Leaving reserveTickets, reservation - {}", reservation);
        return reservation.getReservationId();
    }

    public void createNewTicketAndAddToReservation(Integer serviceId,Integer reservationId, String seatNo){
        LOGGER.info("Entered createNewTicketAndAddToReservation, serviceId - {},reservationId - {},seatNo - {} "
                , serviceId, reservationId, seatNo);
        TicketDetails ticket = new TicketDetails();
        ticket.setServiceId(serviceId);
        ticket.setSeatNumber(seatNo);
        TicketDetails createdTicket = ticketDetailsRepository.save(ticket);

        ReservationMap reservationMap = new ReservationMap();
        reservationMap.setReservationId(reservationId);
        reservationMap.setTicketId(createdTicket.getTicketId());
        reservationMapRepository.save(reservationMap);
        LOGGER.info("Leaving createNewTicketAndAddToReservation");

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

    public Boolean checkIfRequestSeatsAreAlreadyReserved(Integer serviceId, List<String> requestedSeats){
        LOGGER.info("Entered checkIfRequestSeatsAreAlreadyReserved, serviceId - {}, requestedSeats - {}"
                , serviceId, requestedSeats);
        List<String> reservedSeats = getReservedSeats(serviceId);
        Boolean result = customUtilities.checkIfCommonElementsPresentBetweenLists(reservedSeats, requestedSeats);
        LOGGER.info("Leaving checkIfRequestSeatsAreAlreadyReserved, result - {}"
                , result);
        return result;

    }

    @Transactional
    public Integer addToReservationQueue() {
        LOGGER.info("Entered addToReservationQueue");
        ReservationQueue reservationRecord = new ReservationQueue();
        reservationRecord.setStatus("IN-PROGRESS");
        ReservationQueue createdReservationRecord = reservationQueueRepository.save(reservationRecord);
        LOGGER.info("Leaving addToReservationQueue, reservationQueueId - {}"
                , createdReservationRecord.getReservationQueueId());
        return createdReservationRecord.getReservationQueueId();
    }
    @Transactional
    public void reserveTicketAsync(ReserveTicketRequest request, Integer reservationQueueId){
        LOGGER.info("Entered reserveTicketAsync, request - {}", request);
        ReservationQueue reservationQueue = reservationQueueRepository.getReferenceById(reservationQueueId);
        try {
            Integer reservationId  = reserveTickets(request);
            reservationQueue.setReservationId(reservationId);
            reservationQueue.setStatus("COMPLETED");
        } catch (TicketAlreadyReservedException e) {
            LOGGER.info("Ticket is already reserved, updating the queue record to failure");
            reservationQueue.setStatus("FAILED");
        }
        reservationQueueRepository.save(reservationQueue);
        LOGGER.info("Leaving reserveTicketAsync");
    }

    public ReservationQueueResponse getReservationQueueStatus(Integer reservationQueueId) {
        LOGGER.info("Entered getReservationQueueStatus, reservationQueueId - {}", reservationQueueId);
        ReservationQueue reservationRecord = reservationQueueRepository.getReferenceById(reservationQueueId);
        ReservationQueueResponse reservationQueueResponse = modelMapper.map(reservationRecord, ReservationQueueResponse.class);
        LOGGER.info("Leaving getReservationQueueStatus, reservationQueueResponse - {}"
                ,reservationQueueResponse);
        return reservationQueueResponse;
    }
}

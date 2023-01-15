package com.example.TravelReservation.repository;

import com.example.TravelReservation.entity.TicketDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketDetailsRepository extends JpaRepository<TicketDetails, Integer> {

    @Query(value="SELECT SEAT_NUMBER FROM TICKET_DETAILS WHERE SERVICE_ID = ?1", nativeQuery = true)
    public List<String> getReservedSeatNumbersByServiceId(Integer serviceId);
}

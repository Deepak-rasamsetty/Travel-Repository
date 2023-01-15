package com.example.TravelReservation.repository;

import com.example.TravelReservation.entity.ReservationDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationDetailsRepository extends JpaRepository<ReservationDetails, Integer> {
}

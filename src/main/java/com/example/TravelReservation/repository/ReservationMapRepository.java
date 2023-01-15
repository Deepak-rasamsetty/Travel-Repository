package com.example.TravelReservation.repository;

import com.example.TravelReservation.entity.ReservationMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationMapRepository extends JpaRepository<ReservationMap, Integer> {
    List<ReservationMap> findByReservationId(Integer reservationId);
}

package com.example.TravelReservation.repository;

import com.example.TravelReservation.entity.ReservationQueue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationQueueRepository extends JpaRepository<ReservationQueue,Integer> {
}

package com.example.TravelReservation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reservationQueueId;
    private Integer reservationId;
    private String status;
}

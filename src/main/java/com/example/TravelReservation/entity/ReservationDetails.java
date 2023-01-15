package com.example.TravelReservation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer reservationId;
    private Integer serviceId;

    private String boardingLocation;

    private String droppingLocation;


    private Long fare;

    private String createdOn;
}

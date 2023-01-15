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
public class TicketDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer ticketId;

    private Integer userId;
    private Integer serviceId;
    private String seatNumber;
}

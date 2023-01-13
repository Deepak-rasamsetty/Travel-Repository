package com.example.TravelReservation.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BusDetails {
    @Id
    private Integer serviceId;
    private String travels;
    private String rating;

    private String StartLocation;
    private String endLocation;
}

package com.example.TravelReservation.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusDetails {
    @Id
    private Integer serviceId;
    private String travels;
    private String rating;

    private String StartLocation;
    private String endLocation;
}

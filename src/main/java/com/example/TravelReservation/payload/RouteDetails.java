package com.example.TravelReservation.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RouteDetails {
    private Integer destinationSequenceId;
    private String location;
    private Date arrivalTime;
}

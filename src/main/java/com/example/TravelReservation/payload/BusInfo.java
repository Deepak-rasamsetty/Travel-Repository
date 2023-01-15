package com.example.TravelReservation.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusInfo {

    private Integer serviceId;


    private String StartLocation;
    private String endLocation;

    private String travels;
    private String rating;

    private JourneyDetails journeyDetails;
}

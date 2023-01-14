package com.example.TravelReservation.payload;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDetails {

    private Integer sequenceId;
    private String location;
    private String date;
    private String time;
}

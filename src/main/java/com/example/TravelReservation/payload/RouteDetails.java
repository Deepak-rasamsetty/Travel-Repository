package com.example.TravelReservation.payload;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RouteDetails {

    private Integer sequenceId;
    private String location;
    private String date;
    private String time;
}

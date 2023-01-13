package com.example.TravelReservation.payload;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateNewBusServiceRequest {

    private Integer serviceId;
    private String travels;
    private String rating;

    private List<RouteDetails> routes;

}

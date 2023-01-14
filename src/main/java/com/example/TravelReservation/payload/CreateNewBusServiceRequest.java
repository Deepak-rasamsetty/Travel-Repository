package com.example.TravelReservation.payload;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateNewBusServiceRequest {

    private Integer serviceId;
    private String travels;
    private String rating;

    private List<RouteDetails> routes;

}

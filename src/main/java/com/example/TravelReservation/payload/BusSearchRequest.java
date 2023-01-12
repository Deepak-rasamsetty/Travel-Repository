package com.example.TravelReservation.payload;


import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BusSearchRequest {
    @NotNull(message = "Boarding address should not be null")
    private String boardingLocation;
    @NotNull(message = "Destination should not be null")
    private String destination;
}

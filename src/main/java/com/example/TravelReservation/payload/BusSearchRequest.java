package com.example.TravelReservation.payload;


import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BusSearchRequest {
    @NotNull(message = "Boarding Location should not be null")
    private String boardingLocation;
    @NotNull(message = "Dropping Location should not be null")
    private String droppingLocation;
}

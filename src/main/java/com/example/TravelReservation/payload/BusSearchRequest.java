package com.example.TravelReservation.payload;


import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusSearchRequest {
    @NotNull(message = "Boarding Location should not be null")
    private String boardingLocation;
    @NotNull(message = "Dropping Location should not be null")
    private String droppingLocation;
}

package com.example.TravelReservation.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JourneyDetails {
    private String boardingLocation;

    private String droppingLocation;


    private String boardingDate;
    private String droppingDate;
    private String boardingTime;
    private String droppingTime;

    private Long fare;
}

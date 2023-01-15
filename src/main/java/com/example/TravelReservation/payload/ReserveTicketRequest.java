package com.example.TravelReservation.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReserveTicketRequest {
    private Integer serviceId;
    private String boardingLocation;

    private String droppingLocation;
    private List<String> seatNumbers;
}

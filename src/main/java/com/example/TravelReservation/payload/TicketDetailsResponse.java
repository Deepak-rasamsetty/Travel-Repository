package com.example.TravelReservation.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDetailsResponse {
    private String ticketId;
    private String seatNumber;


}

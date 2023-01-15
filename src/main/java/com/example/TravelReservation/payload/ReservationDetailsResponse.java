package com.example.TravelReservation.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDetailsResponse {
    private Integer reservationId;

    private BusInfo BusInfo;
    private List<TicketDetailsResponse> ticketDetails;

    private String createdOn;

}

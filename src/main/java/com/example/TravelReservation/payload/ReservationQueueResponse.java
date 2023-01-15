package com.example.TravelReservation.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationQueueResponse {
    private Integer reservationQueueId;
    private Integer reservationId;
    private String status;
}

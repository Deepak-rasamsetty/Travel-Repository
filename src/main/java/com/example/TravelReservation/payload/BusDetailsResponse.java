package com.example.TravelReservation.payload;

import lombok.*;

import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BusDetailsResponse {
    private Integer serviceId;
    private String travels;
    private String rating;

    private String StartLocation;
    private String endLocation;
    private LocalDateTime boardingTime;
    private LocalDateTime droppingTime;





}

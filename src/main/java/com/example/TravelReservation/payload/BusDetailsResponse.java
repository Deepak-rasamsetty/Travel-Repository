package com.example.TravelReservation.payload;

import lombok.*;

import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BusDetailsResponse {
    private Integer serviceId;


    private String StartLocation;
    private String endLocation;
    private String boardingLocation;

    private String droppingLocation;


    private LocalDate boardingDate;
    private LocalDate droppingDate;
    private String boardingTime;
    private String droppingTime;

    private Long fare;

    private String travels;
    private String rating;





}

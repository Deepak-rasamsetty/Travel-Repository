package com.example.TravelReservation.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "routes")
public class Routes {

    @EmbeddedId
    RoutesPk routesPk;
    private Integer sequenceId;

    private String date;
    private String time;


}

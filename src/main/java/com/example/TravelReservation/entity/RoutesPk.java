package com.example.TravelReservation.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutesPk implements Serializable {
    private Integer serviceId;
    private String location;
}

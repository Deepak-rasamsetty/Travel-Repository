package com.example.TravelReservation.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoutesPk implements Serializable {
    private Integer serviceId;
    private String location;
}

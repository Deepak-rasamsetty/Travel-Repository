package com.example.TravelReservation.payload;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusSearchResponse {

    private String boardingLocation;
    private String droppingLocation;
    private List<BusInfo> busInfo;








}

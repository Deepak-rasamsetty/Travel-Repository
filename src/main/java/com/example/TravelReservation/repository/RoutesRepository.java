package com.example.TravelReservation.repository;

import com.example.TravelReservation.entity.BusDetails;
import com.example.TravelReservation.entity.Routes;
import com.example.TravelReservation.entity.RoutesPk;
import com.example.TravelReservation.payload.BusDetailsResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoutesRepository extends JpaRepository<Routes, RoutesPk> {



    @Query(value = "Select Distinct r1.service_id from routes r1, routes r2 " +
            "where r1.service_id=r2.service_id and r1.location = ?1 and r2.location = ?2 " +
            "and r1.sequence_id< r2.sequence_id", nativeQuery = true)
    public List<Integer> fetchAvailableBusesByLocation(String aLocation, String zLocation);




}

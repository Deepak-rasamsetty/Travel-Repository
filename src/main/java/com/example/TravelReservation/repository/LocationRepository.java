package com.example.TravelReservation.repository;

import com.example.TravelReservation.entity.Location;
import com.example.TravelReservation.payload.RouteDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    boolean existsByName(String location);

    @Query(value = "SELECT NAME FROM LOCATION WHERE UPPER(NAME) LIKE ?1%", nativeQuery = true)
    List<String> findByNameLike(String location);
}

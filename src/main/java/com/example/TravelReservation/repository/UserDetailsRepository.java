package com.example.TravelReservation.repository;

import com.example.TravelReservation.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {


    Boolean existsByEmailAndPassword(String userName, String password);

    UserDetails findByEmail(String email);
}

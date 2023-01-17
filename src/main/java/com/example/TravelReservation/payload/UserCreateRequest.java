package com.example.TravelReservation.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {

    private String name;
    private String email;

    private String mobile;
    private String password;
}
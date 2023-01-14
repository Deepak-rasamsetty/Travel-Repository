package com.example.TravelReservation.controller;

import com.example.TravelReservation.service.LocationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequestMapping("/location")
@RestController
public class LocationController {
    @Autowired
    LocationService locationService;
    private static Logger LOGGER = LogManager.getLogger(LocationController.class);

    @GetMapping("/{location}")
    public List<String> getLocation(@PathVariable("location") String location){
        LOGGER.info("Entered getLocation, location - {}", location);
        List<String> response = locationService.getLocation(location);
        LOGGER.info("Leaving  addNewService, response - {}", response);
        return response;
    }
}

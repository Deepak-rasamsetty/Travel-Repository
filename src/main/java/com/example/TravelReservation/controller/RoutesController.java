package com.example.TravelReservation.controller;

import com.example.TravelReservation.payload.RouteDetails;
import com.example.TravelReservation.service.DetailsService;
import com.example.TravelReservation.service.RoutesService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/routes")
public class RoutesController {
    private static Logger LOGGER = LogManager.getLogger(RoutesController.class);
    @Autowired
    RoutesService routesService;

    @GetMapping("/{serviceId}")
    public List<RouteDetails> getRouteDetails(@PathVariable("serviceId") Integer serviceId){
        LOGGER.info("Entered getRouteDetails, serviceId - {}", serviceId);
        List<RouteDetails> response = routesService.getRouteDetails(serviceId);
        LOGGER.info("Entered getRouteDetails, response - {}", response);
        return response;


    }
}

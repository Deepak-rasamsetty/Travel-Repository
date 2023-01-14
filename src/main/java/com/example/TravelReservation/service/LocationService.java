package com.example.TravelReservation.service;

import com.example.TravelReservation.payload.RouteDetails;
import com.example.TravelReservation.repository.LocationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class LocationService {
    private static Logger LOGGER = LogManager.getLogger(LocationService.class);
    @Autowired
    private LocationRepository locationRepository;

    public Boolean areRoutesValid(List<RouteDetails> routes){
        LOGGER.info("Entered areRoutesValid, routes - {}", routes);
        Predicate<RouteDetails> filterRoutesIfLocationExists = (route)->{
            return !locationRepository.existsByName(route.getLocation());
        };
        Optional<RouteDetails> route = routes.stream().filter(filterRoutesIfLocationExists).findAny();
        Boolean result = route.isEmpty();
        LOGGER.info("Leaving areRoutesValid, result - {}", result);
        return result;
    }

    public List<String> getLocation(String location) {
        LOGGER.info("Entered areRoutesValid, location - {}", location);
        List<String> response = locationRepository.findByNameLike(location.toUpperCase());
        LOGGER.info("Entered areRoutesValid, response - {}", response);
        return response;
    }
}

package com.example.TravelReservation.service;

import com.example.TravelReservation.entity.BusDetails;
import com.example.TravelReservation.entity.Routes;
import com.example.TravelReservation.entity.RoutesPk;
import com.example.TravelReservation.payload.BusDetailsResponse;
import com.example.TravelReservation.payload.BusSearchRequest;
import com.example.TravelReservation.repository.BusDetailsRepository;
import com.example.TravelReservation.repository.RoutesRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DetailsService {
    private static Logger LOGGER = LogManager.getLogger(DetailsService.class);

    @Autowired
    BusDetailsRepository busDetailsRepository;
    @Autowired
    RoutesRepository routesRepository;

    @Autowired
    ModelMapper modelMapper;
    public List<BusDetailsResponse> getAvailableBusses(BusSearchRequest request) {
        LOGGER.info("Entered getAvailableBusses, request - {}", request);
        List<Integer> availableBusList = routesRepository.
                fetchAvailableBusesByLocation(request.getBoardingLocation(), request.getDestination());
        LOGGER.info("Found routes for the given aLocation and zLocation- "+availableBusList);
        List<BusDetails> busDetailsList = busDetailsRepository.findAllById(availableBusList);
        List<BusDetailsResponse> busDetailsResponseList = busDetailsList.stream()
                .map((item)->{return modelMapper.map(item,BusDetailsResponse.class);}).collect(Collectors.toList());
        for(BusDetailsResponse bus: busDetailsResponseList){
            RoutesPk aRouteId=new RoutesPk(bus.getServiceId(), request.getBoardingLocation());
            Optional<Routes> aRoute = routesRepository.findById(aRouteId);
            RoutesPk zRouteId=new RoutesPk(bus.getServiceId(), request.getDestination());
            Optional<Routes> zRoute = routesRepository.findById(zRouteId);
            bus.setBoardingTime(aRoute.get().getArraivalTime());
            bus.setDroppingTime(zRoute.get().getArraivalTime());

        }
        LOGGER.info("Leaving getAvailableBusses, response - {}", Arrays.toString(busDetailsResponseList.toArray()) );
        return busDetailsResponseList;
    }
}

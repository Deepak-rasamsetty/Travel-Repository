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
                fetchAvailableBusesByLocation(request.getBoardingLocation(), request.getDroppingLocation());
        LOGGER.info("Found Buses for the given aLocation and zLocation- {}",availableBusList);
        List<BusDetails> busDetailsList = busDetailsRepository.findAllById(availableBusList);
        LOGGER.info("Fetched busDetails for all available buses - {} ", busDetailsList);
        List<BusDetailsResponse> busDetailsResponseList = busDetailsList.stream()
                .map((item)->{return modelMapper.map(item,BusDetailsResponse.class);}).collect(Collectors.toList());
        updateTimeAndFareDetails(busDetailsResponseList, request);
        LOGGER.info("Leaving getAvailableBusses, response - {}", Arrays.toString(busDetailsResponseList.toArray()) );
        return busDetailsResponseList;
    }

    public void updateTimeAndFareDetails(List<BusDetailsResponse> busDetailsResponseList, BusSearchRequest request){
        LOGGER.info("Entered function updateTimeAndFareDetails , busDetailsResponseList- {} ", busDetailsResponseList);
        for(BusDetailsResponse bus: busDetailsResponseList){
            RoutesPk aRouteId=new RoutesPk(bus.getServiceId(), request.getBoardingLocation());
            Optional<Routes> aRoute = routesRepository.findById(aRouteId);
            RoutesPk zRouteId=new RoutesPk(bus.getServiceId(), request.getDroppingLocation());
            Optional<Routes> zRoute = routesRepository.findById(zRouteId);
            LOGGER.info("Fetched Routes, aRoute - {}, zRoute - {}", aRoute, zRoute);
            aRoute.ifPresent((route)->{ bus.setBoardingTime(route.getTime());});
            aRoute.ifPresent((route)->{ bus.setBoardingDate(route.getDate());});
            aRoute.ifPresent((route)->{ bus.setBoardingLocation(route.getRoutesPk().getLocation());});
            zRoute.ifPresent((route)->{bus.setDroppingTime(route.getTime());});
            zRoute.ifPresent((route)->{bus.setDroppingDate(route.getDate());});
            zRoute.ifPresent((route)->{bus.setDroppingLocation(route.getRoutesPk().getLocation());});
            Long fare = (zRoute.get().getDestinationSequenceId()- aRoute.get().getDestinationSequenceId()+1) *100L;
            bus.setFare(fare);
        }
        LOGGER.info("Leaving function updateTimeAndFareDetails");
    }
}

package com.example.TravelReservation.service;

import com.example.TravelReservation.entity.Routes;
import com.example.TravelReservation.entity.RoutesPk;
import com.example.TravelReservation.payload.BusDetailsResponse;
import com.example.TravelReservation.payload.BusSearchRequest;
import com.example.TravelReservation.payload.CreateNewBusServiceRequest;
import com.example.TravelReservation.payload.RouteDetails;
import com.example.TravelReservation.repository.RoutesRepository;
import com.example.TravelReservation.utility.CustomUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoutesService {
    private static Logger LOGGER = LogManager.getLogger(RoutesService.class);
    @Autowired
    private RoutesRepository routesRepository;

    @Autowired
    CustomUtilities customUtilities;

    public List<RouteDetails> getRouteDetails(Integer serviceId) {
        LOGGER.info("Entered getRouteDetails, serviceId - {}", serviceId);
        List<Routes> routeList = routesRepository.findByRoutesPkServiceIdOrderBySequenceId(serviceId);
        List<RouteDetails> response = routeList.stream().map(customUtilities::convertRouteEntityToRouteDetails).collect(Collectors.toList());
        LOGGER.info("Leaving getRouteDetails, response - {}", response);
        return response;
    }

    public void persistRoutesForNewService(CreateNewBusServiceRequest request){
        LOGGER.info("Entered addRoutes, request - {}", request);
        int sequenceId=1;
        for(RouteDetails requestRoute : request.getRoutes()){
            RoutesPk routesEntityPk =  new RoutesPk(request.getServiceId(), requestRoute.getLocation());
            Routes routesEntity= new Routes(routesEntityPk,sequenceId, requestRoute.getDate(), requestRoute.getTime());
            LOGGER.info("Persisting data for route - {}", routesEntity);
            routesRepository.save(routesEntity);
            sequenceId++;
        }
        LOGGER.info("Leaving addRoutes, request - {}", request);
    }

    public void updateTimeAndFareDetails(List<BusDetailsResponse> busDetailsResponseList, BusSearchRequest request){
        LOGGER.info("Entered  updateTimeAndFareDetails , busDetailsResponseList- {} ", busDetailsResponseList);
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
            Long fare = (zRoute.get().getSequenceId()- aRoute.get().getSequenceId()+1) *100L;
            bus.setFare(fare);
        }
        LOGGER.info("Leaving function updateTimeAndFareDetails");
    }

    public List<Integer> getAvailableBuses(String boardingLocation, String droppingLocation) {
        LOGGER.info("Entered  getAvailableBuses , boardingLocation- {} , droppingLocation- {}", boardingLocation, droppingLocation);
        List<Integer> availableBusList = routesRepository.
                            fetchAvailableBusesByLocation(boardingLocation, droppingLocation);
        LOGGER.info("Leaving  getAvailableBuses , availableBusList- {}", availableBusList);
        return availableBusList;
    }


}

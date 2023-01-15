package com.example.TravelReservation.service;

import com.example.TravelReservation.entity.ReservationDetails;
import com.example.TravelReservation.entity.Routes;
import com.example.TravelReservation.entity.RoutesPk;
import com.example.TravelReservation.payload.*;
import com.example.TravelReservation.repository.RoutesRepository;
import com.example.TravelReservation.utility.CustomUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    @Transactional
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
    public JourneyDetails getJourneyDetails(Integer serviceId, String boardingLocation, String droppingLocation){
        LOGGER.info("Leaving  getJourneyDetails,droppingLocation - {}, boardingLocation - {}, droppingLocation - {}",
                droppingLocation, boardingLocation, droppingLocation);
        JourneyDetails journeyDetails = new JourneyDetails();

        RoutesPk aRouteId=new RoutesPk(serviceId, boardingLocation);
        Optional<Routes> aRoute = routesRepository.findById(aRouteId);
        RoutesPk zRouteId=new RoutesPk(serviceId, droppingLocation);
        Optional<Routes> zRoute = routesRepository.findById(zRouteId);


        aRoute.ifPresent((route)->{ journeyDetails.setBoardingTime(route.getTime());});
        aRoute.ifPresent((route)->{ journeyDetails.setBoardingDate(route.getDate());});
        aRoute.ifPresent((route)->{ journeyDetails.setBoardingLocation(route.getRoutesPk().getLocation());});
        zRoute.ifPresent((route)->{journeyDetails.setDroppingTime(route.getTime());});
        zRoute.ifPresent((route)->{journeyDetails.setDroppingDate(route.getDate());});
        zRoute.ifPresent((route)->{journeyDetails.setDroppingLocation(route.getRoutesPk().getLocation());});

        Long fare = (zRoute.get().getSequenceId()- aRoute.get().getSequenceId()+1) *100L;
        journeyDetails.setFare(fare);
        LOGGER.info("Leaving  getJourneyDetails, journeyDetails - {}", journeyDetails);
        return journeyDetails;

    }
    public List<Integer> getAvailableBuses(String boardingLocation, String droppingLocation) {
        LOGGER.info("Entered  getAvailableBuses , boardingLocation- {} , droppingLocation- {}", boardingLocation, droppingLocation);
        List<Integer> availableBusList = routesRepository.
                            fetchAvailableBusesByLocation(boardingLocation, droppingLocation);
        LOGGER.info("Leaving  getAvailableBuses , availableBusList- {}", availableBusList);
        return availableBusList;
    }


}

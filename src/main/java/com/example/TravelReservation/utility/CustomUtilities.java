package com.example.TravelReservation.utility;

import com.example.TravelReservation.entity.BusDetails;
import com.example.TravelReservation.entity.Routes;
import com.example.TravelReservation.payload.CreateNewBusServiceRequest;
import com.example.TravelReservation.payload.RouteDetails;
import com.example.TravelReservation.service.DetailsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@Component
public class CustomUtilities {

    private static Logger LOGGER = LogManager.getLogger(CustomUtilities.class);

    @Autowired
    ModelMapper modelMapper;

    public void sortRoutesList(CreateNewBusServiceRequest request){
        LOGGER.info("Entered sortRoutesList, request - {}", request);
        Comparator<RouteDetails> routeDetailsListSort = (obj1, obj2)->{
            if(obj1.getSequenceId() < obj2.getSequenceId()) return -1;
            if(obj1.getSequenceId() > obj2.getSequenceId()) return 1;
            return 0;
        };
        Collections.sort(request.getRoutes(), routeDetailsListSort);
        LOGGER.info("RouteList sorted , routeList - {}", request.getRoutes());
        LOGGER.info("Leaving sortRoutesList");
    }

    public void updateStartAndEndLocations(BusDetails busDetails, List<RouteDetails> routeList){
        LOGGER.info("Entered updateStartAndEndLocations, busDetails - {}", busDetails);
        Function<List<RouteDetails>, String> getStartLocation = (list)->{return list.get(0).getLocation();};
        Function<List<RouteDetails>, String> getEndLocation = (list)->{return list.get(list.size()-1).getLocation();};
        busDetails.setStartLocation(getStartLocation.apply(routeList));
        busDetails.setEndLocation(getEndLocation.apply(routeList));
        LOGGER.info("Leaving updateStartAndEndLocations, busDetails - {}", busDetails);

    }

    public RouteDetails convertRouteEntityToRouteDetails(Routes route){
        LOGGER.info("Entered convertRouteEntityToRouteDetails, route - {}", route);
        RouteDetails routeDetails = modelMapper.map(route,RouteDetails.class );
        routeDetails.setLocation(route.getRoutesPk().getLocation());
        LOGGER.info("Leaving convertRouteEntityToRouteDetails, routeDetails - {}", routeDetails);
        return routeDetails;

    }

    public Boolean checkIfCommonElementsPresentBetweenLists(List<String> list1, List<String> list2){
        LOGGER.info("Entered checkIfList2ElementAvailableInList1, list1 - {}, list2 - {}", list1, list2);
        Predicate<String> isElementAVailableInList1 = (element)->{
          return list1.contains(element);
        };
        Optional<String> commonElement = list2.stream().filter(isElementAVailableInList1).findAny();
        Boolean result = commonElement.isPresent();
        LOGGER.info("leaving  checkIfList2ElementAvailableInList1, result - {}", result);
        return result;
    }
}

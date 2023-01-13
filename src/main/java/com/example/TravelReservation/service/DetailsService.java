package com.example.TravelReservation.service;

import com.example.TravelReservation.exceptions.LocationIsInvalidException;
import com.example.TravelReservation.exceptions.ServiceAlreadyPresentException;
import com.example.TravelReservation.entity.BusDetails;
import com.example.TravelReservation.entity.Routes;
import com.example.TravelReservation.entity.RoutesPk;
import com.example.TravelReservation.payload.*;
import com.example.TravelReservation.repository.BusDetailsRepository;
import com.example.TravelReservation.repository.LocationRepository;
import com.example.TravelReservation.repository.RoutesRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class DetailsService {
    private static Logger LOGGER = LogManager.getLogger(DetailsService.class);

    @Autowired
    private BusDetailsRepository busDetailsRepository;
    @Autowired
    private RoutesRepository routesRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ModelMapper modelMapper;

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

    @Transactional
    public CustomResponse addNewService(CreateNewBusServiceRequest request) throws ServiceAlreadyPresentException, LocationIsInvalidException {
        LOGGER.info("Entered addNewService, request - {}", request);

        if(checkIfServiceAlreadyExists(request.getServiceId())){
            throw new ServiceAlreadyPresentException();
        }
       if(!areRoutesValid(request.getRoutes())){
           throw new LocationIsInvalidException();
       }
        BusDetails busDetailsToBeAdded = modelMapper.map(request, BusDetails.class);
        sortRoutesList(request);
        updateStartAndEndLocations(busDetailsToBeAdded, request.getRoutes());
        persistRoutesForNewService(request);
        persistBusDetailsForNewService(busDetailsToBeAdded);
        CustomResponse response=new CustomResponse("Successfully added new service");
        LOGGER.info("Leaving function addNewService, response - {}", response);
        return response;
    }

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

    private void persistBusDetailsForNewService(BusDetails busDetailsToBeAdded) {
        LOGGER.info("Entered persistBusDetailsForNewService, busDetailsToBeAdded - {}", busDetailsToBeAdded);
        busDetailsRepository.save(busDetailsToBeAdded);
        LOGGER.info("Leaving persistBusDetailsForNewService, data persisted successfully");
    }




    public Boolean checkIfServiceAlreadyExists(Integer serviceId){
        LOGGER.info("Entered checkIfServiceAlreadyExists, serviceId - {}", serviceId);
        Function<Integer, Boolean> checkBusServiceAlreadyExistsById = busDetailsRepository::existsById;
        Boolean result = checkBusServiceAlreadyExistsById.apply(serviceId);
        LOGGER.info("Leaving checkIfServiceAlreadyExists, result - {}", result);
        return result;
    }

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

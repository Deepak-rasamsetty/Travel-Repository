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
import com.example.TravelReservation.utility.CustomUtilities;
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
    private LocationService locationService;
    @Autowired
    private RoutesService routesService;
    @Autowired
    private CustomUtilities customUtilities;
    @Autowired
    private ModelMapper modelMapper;

    public List<BusDetailsResponse> getAvailableBusses(BusSearchRequest request) {
        LOGGER.info("Entered getAvailableBusses, request - {}", request);
        List<Integer> availableBusList = routesService.getAvailableBuses(request.getBoardingLocation(), request.getDroppingLocation());
        List<BusDetails> busDetailsList = busDetailsRepository.findAllById(availableBusList);
        LOGGER.info("Fetched busDetails for all available buses - {} ", busDetailsList);
        List<BusDetailsResponse> busDetailsResponseList = busDetailsList.stream()
                .map((item)->{return modelMapper.map(item,BusDetailsResponse.class);}).collect(Collectors.toList());
        routesService.updateTimeAndFareDetails(busDetailsResponseList, request);
        LOGGER.info("Leaving getAvailableBusses, response - {}", Arrays.toString(busDetailsResponseList.toArray()) );
        return busDetailsResponseList;
    }
    @Transactional
    public CustomResponse addNewService(CreateNewBusServiceRequest request) throws ServiceAlreadyPresentException, LocationIsInvalidException {
        LOGGER.info("Entered addNewService, request - {}", request);
        if(checkIfServiceAlreadyExists(request.getServiceId())){
            throw new ServiceAlreadyPresentException();
        }
       if(!locationService.areRoutesValid(request.getRoutes())){
           throw new LocationIsInvalidException();
       }
        BusDetails busDetailsToBeAdded = modelMapper.map(request, BusDetails.class);
        customUtilities.sortRoutesList(request);
        customUtilities.updateStartAndEndLocations(busDetailsToBeAdded, request.getRoutes());
        routesService.persistRoutesForNewService(request);
        persistBusDetailsForNewService(busDetailsToBeAdded);
        CustomResponse response=new CustomResponse("Successfully added new service");
        LOGGER.info("Leaving function addNewService, response - {}", response);
        return response;
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


}

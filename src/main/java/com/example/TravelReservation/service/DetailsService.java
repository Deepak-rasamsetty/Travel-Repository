package com.example.TravelReservation.service;

import com.example.TravelReservation.exceptions.LocationIsInvalidException;
import com.example.TravelReservation.exceptions.ServiceAlreadyPresentException;
import com.example.TravelReservation.entity.BusDetails;
import com.example.TravelReservation.payload.*;
import com.example.TravelReservation.repository.BusDetailsRepository;
import com.example.TravelReservation.utility.CustomUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

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

    public BusSearchResponse getAvailableBusses(BusSearchRequest request) {
        LOGGER.info("Entered getAvailableBusses, request - {}", request);
        List<Integer> availableBusList = routesService.getAvailableBuses(request.getBoardingLocation(), request.getDroppingLocation());
        List<BusDetails> busDetailsList = busDetailsRepository.findAllById(availableBusList);
        LOGGER.info("Fetched busDetails for all available buses - {} ", busDetailsList);
        BusSearchResponse busSearchResponse = new BusSearchResponse();
        List<BusInfo> busInfoList = new ArrayList<>();
        Consumer<BusDetails> addToBusInfoList =(busDetails)->{
            BusInfo busInfo = getBusDetails(busDetails.getServiceId());
            JourneyDetails journeyDetails = routesService.getJourneyDetails(busDetails.getServiceId(),
                    request.getBoardingLocation(), request.getDroppingLocation());
            busInfo.setJourneyDetails(journeyDetails);
            busInfoList.add(busInfo);
        };
        busDetailsList.stream().forEach(addToBusInfoList);
        busSearchResponse.setBoardingLocation(request.getBoardingLocation());
        busSearchResponse.setDroppingLocation(request.getDroppingLocation());
        busSearchResponse.setBusInfo(busInfoList);
        LOGGER.info("Leaving getAvailableBusses, response - {}", busSearchResponse );
        return busSearchResponse;
    }

    public BusInfo getBusDetails(Integer serviceId){
        LOGGER.info("Entered getBusDetails, serviceId - {}", serviceId);
        Optional<BusDetails> busDetails = busDetailsRepository.findById(serviceId);
        LOGGER.info("Fetched busDetails for service Id - {}, Bus details - {} ", serviceId, busDetails);

        if(busDetails.isEmpty()){
             return null;
        }
        BusInfo busInfo = modelMapper.map(busDetails.get(),BusInfo.class);

        LOGGER.info("Leaving getBusDetails, busInfo - {}", busInfo);
        return busInfo;
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

package com.example.TravelReservation.controller;

import com.example.TravelReservation.ServiceAlreadyPresentException;
import com.example.TravelReservation.payload.BusDetailsResponse;
import com.example.TravelReservation.payload.BusSearchRequest;
import com.example.TravelReservation.payload.CreateNewBusServiceRequest;
import com.example.TravelReservation.payload.CustomResponse;
import com.example.TravelReservation.service.DetailsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/bus")
public class DetailsController {
    private static Logger LOGGER = LogManager.getLogger(DetailsController.class);
    @Autowired
    DetailsService detailsService;
   @PostMapping("/")
    public List<BusDetailsResponse> getAvailableBuses(@Valid @RequestBody BusSearchRequest request){
       LOGGER.info("Entered getAvailableBuses, request - {}", request);
       List<BusDetailsResponse> resposneList= detailsService.getAvailableBusses(request);
       LOGGER.info("Leaving getAvailableBuses, response - {}", resposneList);
       return resposneList;
   }

   @PostMapping("/add")
    public CustomResponse addNewService(@Valid @RequestBody CreateNewBusServiceRequest request) throws ServiceAlreadyPresentException {
       LOGGER.info("Entered addNewService, request - {}", request);
       CustomResponse response = detailsService.addNewService(request);
       LOGGER.info("Leaving  addNewService, response - {}", response);
       return response;

   }

}

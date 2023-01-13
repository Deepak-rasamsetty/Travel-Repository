package com.example.TravelReservation;

import com.example.TravelReservation.entity.BusDetails;
import com.example.TravelReservation.entity.Location;
import com.example.TravelReservation.entity.Routes;
import com.example.TravelReservation.entity.RoutesPk;
import com.example.TravelReservation.repository.BusDetailsRepository;
import com.example.TravelReservation.repository.LocationRepository;
import com.example.TravelReservation.repository.RoutesRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@SpringBootApplication
public class TravelReservationApplication {

	@Autowired
	RoutesRepository routesRepository;
	@Autowired
	BusDetailsRepository busDetailsRepository;

	@Autowired
	LocationRepository locationRepository;
	public static void main(String[] args) {
		SpringApplication.run(TravelReservationApplication.class, args);
	}

	@Bean
	public ModelMapper getModelmapper(){
		return new ModelMapper();
	}

	@Bean
	public void addLocation(){
		Location l1= new Location();
		Location l2= new Location();
		Location l3= new Location();
		Location l4= new Location();
		Location l5= new Location();
		Location l6= new Location();
		Location l7= new Location();
		Location l8= new Location();
		Location l9= new Location();

		l1.setName("HYDERABAD");
		l2.setName("CHENNAI");
		l3.setName("MUMBAI");
		l4.setName("PUNE");
		l5.setName("BANGLORE");
		l6.setName("KOCHI");

		l7.setName("KOLKATA");
		l8.setName("NAGPUR");
		l9.setName("RANCHI");

		locationRepository.save(l1);
		locationRepository.save(l2);
		locationRepository.save(l3);
		locationRepository.save(l4);
		locationRepository.save(l5);
		locationRepository.save(l6);
		locationRepository.save(l7);
		locationRepository.save(l8);
		locationRepository.save(l9);


	}
//	@Bean
//	public void addData(){
//		Routes r1=new Routes();
//		RoutesPk rpk1=new RoutesPk();
//		rpk1.setServiceId(1234);
//		rpk1.setLocation("Hyderabad");
//		r1.setRoutesPk(rpk1);
//		r1.setDestinationSequenceId(1);
//		r1.setDate(LocalDate.of(2022,02,01));
//		r1.setTime("22:00");
//
//		Routes r2=new Routes();
//		RoutesPk rpk2=new RoutesPk();
//		rpk2.setServiceId(1234);
//		rpk2.setLocation("Banglore");
//		r2.setRoutesPk(rpk2);
//		r2.setDestinationSequenceId(2);
//		r2.setDate(LocalDate.of(2022,02,01));
//		r2.setTime("23:00");
//
//		Routes r3=new Routes();
//		RoutesPk rpk3=new RoutesPk();
//		rpk3.setServiceId(1234);
//		rpk3.setLocation("Chennai");
//		r3.setRoutesPk(rpk3);
//		r3.setDestinationSequenceId(3);
//		r3.setDate(LocalDate.of(2022,02,02));
//		r3.setTime("00:00");
//
//		Routes r4=new Routes();
//		RoutesPk rpk4=new RoutesPk();
//		rpk4.setServiceId(1234);
//		rpk4.setLocation("Kanyakumari");
//		r4.setRoutesPk(rpk4);
//		r4.setDestinationSequenceId(4);
//		r4.setDate(LocalDate.of(2022,02,02));
//		r4.setTime("01:00");
//
//		Routes r5=new Routes();
//		RoutesPk rpk5=new RoutesPk();
//		rpk5.setServiceId(4567);
//		rpk5.setLocation("Banglore");
//		r5.setRoutesPk(rpk5);
//		r5.setDestinationSequenceId(1);
//		r5.setDate(LocalDate.of(2022,02,02));
//		r5.setTime("03:00");
//
//		Routes r6=new Routes();
//		RoutesPk rpk6=new RoutesPk();
//		rpk6.setServiceId(4567);
//		rpk6.setLocation("Chennai");
//		r6.setRoutesPk(rpk6);
//		r6.setDestinationSequenceId(2);
//		r6.setDate(LocalDate.of(2022,02,02));
//		r6.setTime("04:00");
//
//		Routes r7=new Routes();
//		RoutesPk rpk7=new RoutesPk();
//		rpk7.setServiceId(4567);
//		rpk7.setLocation("Kanyakumari");
//		r7.setRoutesPk(rpk7);
//		r7.setDestinationSequenceId(3);
//		r7.setDate(LocalDate.of(2022,02,02));
//		r7.setTime("05:00");
//
//		Routes r8=new Routes();
//		RoutesPk rpk8=new RoutesPk();
//		rpk8.setServiceId(4567);
//		rpk8.setLocation("Delhi");
//		r8.setRoutesPk(rpk8);
//		r8.setDestinationSequenceId(4);
//		r8.setDate(LocalDate.of(2022,02,02));
//		r8.setTime("06:00");
//
//		routesRepository.save(r1);
//		routesRepository.save(r2);
//		routesRepository.save(r3);
//		routesRepository.save(r4);
//
//		routesRepository.save(r5);
//		routesRepository.save(r6);
//		routesRepository.save(r7);
//		routesRepository.save(r8);
//
//		BusDetails b1=new BusDetails();
//		b1.setTravels("MorningStar");
//		b1.setServiceId(1234);
//		b1.setRating("3.5");
//		b1.setStartLocation("Hyderabad");
//		b1.setEndLocation("kanyakumari");
//
//		BusDetails b2=new BusDetails();
//		b2.setTravels("Kaveri");
//		b2.setServiceId(4567);
//		b2.setRating("3.5");
//		b2.setStartLocation("Banglore");
//		b2.setEndLocation("Delhi");
//
//		busDetailsRepository.save(b1);
//		busDetailsRepository.save(b2);
//
//	}
}

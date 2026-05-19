package com.ticketbooking.busservice.service.impl;
import com.ticketbooking.busservice.constants.BusType;
import com.ticketbooking.busservice.constants.ErrorCodeEnum;
import com.ticketbooking.busservice.constants.SeatType;
import com.ticketbooking.busservice.dto.*;
import com.ticketbooking.busservice.entity.*;
import com.ticketbooking.busservice.exception.CustomException;
import com.ticketbooking.busservice.repo.*;
import com.ticketbooking.busservice.service.BusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusServiceImpl implements BusService {

    private final BusRepository busRepository;
    private final RouteRepository routeRepository;
    private final ScheduleRepository scheduleRepository;
    private final SeatRepository seatRepository;

    // ═══════════════════════════════
    // ADMIN
    // ═══════════════════════════════

    @Override
    public void addBus(BusRequest request) {

        if(busRepository.existsByBusName(request.getName())){
            throw new CustomException(ErrorCodeEnum.BUS_ALREADY_PRESENT.getCode(),
                    ErrorCodeEnum.BUS_ALREADY_PRESENT.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        Bus bus = new Bus();

        bus.setBusName(request.getName());
        bus.setBusType(BusType.valueOf(request.getBusType()));
        bus.setBusSeat(request.getTotalSeats());
        busRepository.save(bus);
    }

    @Override
    public void addRoute(RouteRequest request) {
        if(routeRepository.existsByOriginAndDestination(request.getOrigin(), request.getDestination())){
            throw  new CustomException(ErrorCodeEnum.ROUTE_ALREADY_EXISTS.getCode(),
                    ErrorCodeEnum.ROUTE_ALREADY_EXISTS.getMessage(),
                    HttpStatus.ALREADY_REPORTED);
        }

        Route route = new Route();
        route.setOrigin(request.getOrigin());
        route.setDestination(request.getDestination());
        route.setDistanceKm(request.getDistanceKm());
        routeRepository.save(route);
    }

    @Override
    public void addSchedule(ScheduleRequest request) {
        if(scheduleRepository.existsByBusIdAndRouteIdAndTravelDate(request.getBusId(),
                request.getRouteId(),
                request.getTravelDate())){
            throw new CustomException(
                    ErrorCodeEnum.SCHEDULE_ALREADY_EXISTS.getCode(),
                    ErrorCodeEnum.SCHEDULE_ALREADY_EXISTS.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
        busRepository.findById(request.getBusId());
        Bus bus = busRepository.findById(request.getBusId())
                .orElseThrow(()-> new CustomException(ErrorCodeEnum.BUS_NOT_FOUND.getCode(),
                        ErrorCodeEnum.BUS_NOT_FOUND.getMessage(),
                        HttpStatus.NOT_FOUND) );

        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(()-> new CustomException(ErrorCodeEnum.ROUTE_NOT_FOUND.getCode(),
                        ErrorCodeEnum.ROUTE_NOT_FOUND.getMessage(),
                        HttpStatus.NOT_FOUND));

        Schedule schedule = new Schedule();
        schedule.setBus(bus);
        schedule.setRoute(route);
        schedule.setTravelDate(request.getTravelDate());
        schedule.setDepartureTime(request.getDepartureTime());
        schedule.setArrivalTime(request.getArrivalTime());
        schedule.setPrice(request.getPrice());
        schedule.setAvailableSeat(bus.getBusSeat());
        scheduleRepository.save(schedule);

        // Seats automatically banao
        createSeatsForSchedule(schedule, bus.getBusSeat());
    }

    private void createSeatsForSchedule(
            Schedule schedule, int totalSeats) {
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= totalSeats; i++) {
            Seat seat = new Seat();
            seat.setSchedule(schedule);
            seat.setSeatNumber("S" + i);
            seat.setIsBooked(false);
            seat.setSeatType(i % 2 != 0 ?
                    SeatType.WINDOW :
                    SeatType.AISLE);
            seats.add(seat);
        }
        seatRepository.saveAll(seats);
    }

    // ═══════════════════════════════
    // CUSTOMER
    // ═══════════════════════════════

    @Override
    public List<BusSearchResponse> searchBuses(
            String origin,
            String destination,
            LocalDate travelDate) {


        List<Schedule> schedules = scheduleRepository
                .findByRoute_OriginAndRoute_DestinationAndTravelDate(
                        origin, destination, travelDate);

        if(schedules.isEmpty()){
             throw  new CustomException(ErrorCodeEnum.SCHEDULE_NOT_FOUND.getCode(),
                     ErrorCodeEnum.SCHEDULE_NOT_FOUND.getMessage(),
                     HttpStatus.NOT_FOUND);
        }

        List<BusSearchResponse> responses = new ArrayList<>();
        for (Schedule schedule : schedules) {
            BusSearchResponse response = new BusSearchResponse();
            response.setScheduleId(schedule.getId());
            response.setBusName(schedule.getBus().getBusName());
            response.setBusType(schedule.getBus()
                    .getBusType().name());
            response.setOrigin(schedule.getRoute().getOrigin());
            response.setDestination(schedule.getRoute()
                    .getDestination());
            response.setTravelDate(schedule.getTravelDate());
            response.setDepartureTime(schedule.getDepartureTime());
            response.setArrivalTime(schedule.getArrivalTime());
            response.setPrice(schedule.getPrice());
            response.setAvailableSeats(schedule.getAvailableSeat());
            responses.add(response);
        }
        return responses;
    }

    @Override
    public List<SeatResponse> getAvailableSeats(Long scheduleId) {
        scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(
                        ErrorCodeEnum.SCHEDULE_NOT_FOUND.getCode(),
                        ErrorCodeEnum.SCHEDULE_NOT_FOUND.getMessage(),
                        HttpStatus.NOT_FOUND
                ));

        List<Seat> seats = seatRepository
                .findByScheduleIdAndIsBooked(scheduleId, false);

        List<SeatResponse> responses = new ArrayList<>();
        for (Seat seat : seats) {
            SeatResponse response = new SeatResponse();
            response.setSeatId(seat.getId());
            response.setSeatNumber(seat.getSeatNumber());
            response.setSeatType(seat.getSeatType().name());
            response.setIsBooked(seat.getIsBooked());
            responses.add(response);
        }
        return responses;
    }
    @Override
    public void lockSeat(Long seatId) {

        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(()-> new CustomException(ErrorCodeEnum.SEAT_NOT_FOUND.getCode(),
                        ErrorCodeEnum.SEAT_NOT_FOUND.getMessage(),
                        HttpStatus.NOT_FOUND));


        if(seat.getIsBooked()){
            throw  new CustomException(ErrorCodeEnum.BUS_ALREADY_PRESENT.getCode(),
                    ErrorCodeEnum.BUS_ALREADY_PRESENT.getMessage(),
                    HttpStatus.ALREADY_REPORTED);
        }
        seat.setIsBooked(true);
        seatRepository.save(seat);

        // Available seats ghatao
        Schedule schedule = seat.getSchedule();
        schedule.setAvailableSeat(
                schedule.getAvailableSeat() - 1);
        scheduleRepository.save(schedule);
    }

    @Override
    public void unlockSeat(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new CustomException(
                        ErrorCodeEnum.SEAT_NOT_FOUND.getCode(),
                        ErrorCodeEnum.SEAT_NOT_FOUND.getMessage(),
                        HttpStatus.NOT_FOUND
                ));

        // Check — pehle se unbooked hai?
        if (!seat.getIsBooked()) {
            throw new CustomException(
                    ErrorCodeEnum.SEAT_ALREADY_BOOKED.getCode(),
                    ErrorCodeEnum.SEAT_ALREADY_BOOKED.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }

        seat.setIsBooked(false);
        seatRepository.save(seat);

        Schedule schedule = seat.getSchedule();
        schedule.setAvailableSeat(
                schedule.getAvailableSeat() + 1);
        scheduleRepository.save(schedule);
    }
}
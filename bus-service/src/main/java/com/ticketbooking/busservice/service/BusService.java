package com.ticketbooking.busservice.service;


import com.ticketbooking.busservice.dto.*;
import java.time.LocalDate;
import java.util.List;

public interface BusService {

    // Admin
    void addBus(BusRequest request);
    void addRoute(RouteRequest request);
    void addSchedule(ScheduleRequest request);

    // Customer
    List<BusSearchResponse> searchBuses(
            String origin,
            String destination,
            LocalDate travelDate);
    List<SeatResponse> getAvailableSeats(Long scheduleId);

    // Internal — Booking Service ke liye
    void lockSeat(Long seatId);
    void unlockSeat(Long seatId);
}
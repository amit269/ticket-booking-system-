package com.ticketbooking.busservice.controller;

import com.ticketbooking.busservice.dto.BusRequest;
import com.ticketbooking.busservice.dto.RouteRequest;
import com.ticketbooking.busservice.dto.ScheduleRequest;
import com.ticketbooking.busservice.service.BusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/buses")
@RequiredArgsConstructor
public class AdminBusController {

    private final BusService busService;

    @PostMapping("/add-bus")
    public ResponseEntity<String> addBus(
           @Valid @RequestBody BusRequest request) {
        busService.addBus(request);
        return new  ResponseEntity("Bus added successfully" , HttpStatus.CREATED
        );
    }

    @PostMapping("/add-route")
    public ResponseEntity<String> addRoute(
          @Valid  @RequestBody RouteRequest request) {
        busService.addRoute(request);
        return new  ResponseEntity(
                "Route added successfully", HttpStatus.CREATED
        );
    }

    @PostMapping("/add-schedule")
    public ResponseEntity<String> addSchedule(
          @Valid  @RequestBody ScheduleRequest request) {
        busService.addSchedule(request);
        return new  ResponseEntity(" schedule added successfully" ,HttpStatus.CREATED);

    }
}
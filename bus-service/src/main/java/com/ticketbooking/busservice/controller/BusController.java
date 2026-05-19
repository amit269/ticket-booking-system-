package com.ticketbooking.busservice.controller;

import com.ticketbooking.busservice.dto.BusSearchResponse;
import com.ticketbooking.busservice.dto.SeatResponse;
import com.ticketbooking.busservice.service.BusService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/buses")
@RequiredArgsConstructor
public class BusController {

    private final BusService busService;

    @GetMapping("/search")
    public ResponseEntity<List<BusSearchResponse>> searchBuses(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate travelDate) {
        return ResponseEntity.ok(
                busService.searchBuses(origin, destination, travelDate)
        );
    }

    @GetMapping("/{scheduleId}/seats")
    public ResponseEntity<List<SeatResponse>> getAvailableSeats(
            @PathVariable Long scheduleId) {
        return ResponseEntity.ok(
                busService.getAvailableSeats(scheduleId)
        );
    }
}
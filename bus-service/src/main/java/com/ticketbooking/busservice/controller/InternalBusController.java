package com.ticketbooking.busservice.controller;
import com.ticketbooking.busservice.service.BusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/buses")
@RequiredArgsConstructor
public class InternalBusController {

    private final BusService busService;

    @PutMapping("/seats/{seatId}/lock")
    public ResponseEntity<String> lockSeat(
            @PathVariable Long seatId) {
        busService.lockSeat(seatId);
        return ResponseEntity.ok("Seat locked successfully");
    }

    @PutMapping("/seats/{seatId}/unlock")
    public ResponseEntity<String> unlockSeat(
            @PathVariable Long seatId) {
        busService.unlockSeat(seatId);
        return ResponseEntity.ok("Seat unlocked successfully");
    }
}
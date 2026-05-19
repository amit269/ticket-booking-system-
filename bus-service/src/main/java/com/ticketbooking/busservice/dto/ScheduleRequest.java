package com.ticketbooking.busservice.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleRequest {
    @NotNull(message = "Bus ID is required")
    private Long busId;
    @NotNull(message = "Route ID is required")
    private Long routeId;
    @NotNull(message = "Travel date  is required")
    private LocalDate travelDate;
    @NotNull(message = "Departure date  is required")
    private LocalTime departureTime;
    @NotNull(message = "Arrival date  is required")
    private LocalTime arrivalTime;
    @NotNull(message = "Price is required")
    private BigDecimal price;
}
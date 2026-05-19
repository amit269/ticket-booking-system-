package com.ticketbooking.busservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RouteRequest {
    @NotBlank(message = "Origin is required")
    private String origin;
    @NotBlank(message = "Destination is required")
    private String destination;
    @NotNull(message = "Distance is required")
    @Min(value = 1, message = "Distance must be at least 1 km")
    private Integer distanceKm;
}

package com.ticketbooking.busservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SeatResponse {
    private Long seatId;
    private String seatNumber;
    private String seatType;
    private Boolean isBooked;
}
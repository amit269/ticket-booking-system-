package com.ticketbooking.busservice.constants;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum{
    GENERIC_ERROR("20000" , " Something went wrong please"),
    BUS_ALREADY_PRESENT("20001" , "Bus already exists"),
    ROUTE_ALREADY_EXISTS("20002" , "Route already exists"),
    BUS_NOT_FOUND("20003" , "Bus not Found "),
    ROUTE_NOT_FOUND("20004" , "Route not found"),
    SEAT_ALREADY_BOOKED("20005", "Seat already booked try another one"),
    SEAT_NOT_FOUND("20006" , " Seat not found"),
    SCHEDULE_NOT_FOUND("20007" , "No Schedule found"),
    SCHEDULE_ALREADY_EXISTS("20008" , "schedule already exist"),
    INVALID_INPUT("20009" , "Validation Failed");
    private final String code;
    private final String message;

    ErrorCodeEnum(String code , String message){
        this.code = code;
        this.message = message;

    }


}

package com.ticketbooking.busservice.repo;

import com.ticketbooking.busservice.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByRoute_OriginAndRoute_DestinationAndTravelDate(
            String origin,
            String destination,
            LocalDate travelDate
    );
    boolean existsByBusIdAndRouteIdAndTravelDate(
            Long busId,
            Long routeId,
            LocalDate travelDate
    );
}

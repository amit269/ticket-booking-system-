package com.ticketbooking.busservice.repo;

import com.ticketbooking.busservice.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long>{
    List<Seat> findByScheduleId(Long scheduleId);

    List<Seat> findByScheduleIdAndIsBooked(Long scheduleId, Boolean isBooked);
}

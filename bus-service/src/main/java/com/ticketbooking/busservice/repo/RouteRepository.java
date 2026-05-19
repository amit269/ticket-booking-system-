package com.ticketbooking.busservice.repo;

import com.ticketbooking.busservice.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route , Long> {
    boolean existsByOriginAndDestination(String origin, String destination);
}

package com.ticketbooking.busservice.entity;

import com.ticketbooking.busservice.constants.SeatType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "seats")
public class Seat{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String seatNumber;

    @ManyToOne
    @JoinColumn(name = "schedule_id",nullable = false)
    private Schedule schedule;

    @Column(nullable = false)
    private Boolean isBooked = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatType seatType;

}

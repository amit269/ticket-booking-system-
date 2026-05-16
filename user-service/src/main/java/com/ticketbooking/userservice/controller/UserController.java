package com.ticketbooking.userservice.controller;


import com.ticketbooking.userservice.dto.ProfileResponse;

import com.ticketbooking.userservice.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getProfile() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        ProfileResponse profile = userService.getProfile(email);
        return ResponseEntity.ok(profile);
    }
}
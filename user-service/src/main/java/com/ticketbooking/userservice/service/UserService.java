package com.ticketbooking.userservice.service;


import com.ticketbooking.userservice.dto.AuthResponse;
import com.ticketbooking.userservice.dto.LoginRequest;
import com.ticketbooking.userservice.dto.ProfileResponse;
import com.ticketbooking.userservice.dto.RegisterRequest;
import com.ticketbooking.userservice.entity.User;

public interface UserService {

    void register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    ProfileResponse getProfile(String email);
}
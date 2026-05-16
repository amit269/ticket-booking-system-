package com.ticketbooking.userservice.dto;

import com.ticketbooking.userservice.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class ProfileResponse {
    String name;
    String email;
    Role role;
}

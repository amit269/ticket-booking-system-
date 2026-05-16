package com.ticketbooking.userservice.service;

import com.ticketbooking.userservice.config.JwtUtil;
import com.ticketbooking.userservice.constant.ErrorCodeEnum;
import com.ticketbooking.userservice.constant.Role;
import com.ticketbooking.userservice.dto.AuthResponse;
import com.ticketbooking.userservice.dto.LoginRequest;
import com.ticketbooking.userservice.dto.ProfileResponse;
import com.ticketbooking.userservice.dto.RegisterRequest;
import com.ticketbooking.userservice.entity.User;
import com.ticketbooking.userservice.exception.CustomException;
import com.ticketbooking.userservice.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {

            throw new CustomException(
                    ErrorCodeEnum.EMAIL_ALREADY_EXISTS.getCode(),
                    ErrorCodeEnum.EMAIL_ALREADY_EXISTS.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);


    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(
                        ErrorCodeEnum.EMAIL_NOT_FOUND.getCode(),
                        ErrorCodeEnum.EMAIL_NOT_FOUND.getMessage(),
                        HttpStatus.NOT_FOUND
                ));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(
                    ErrorCodeEnum.WRONG_PASSWORD.getCode(),
                    ErrorCodeEnum.WRONG_PASSWORD.getMessage(),
                    HttpStatus.UNAUTHORIZED
            );
        }

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );
        return new AuthResponse(token, user.getName(), user.getEmail());
    }

    @Override
    public ProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new CustomException(
                ErrorCodeEnum.USER_NOT_FOUND.getCode(),
                ErrorCodeEnum.USER_NOT_FOUND.getMessage(),
                HttpStatus.BAD_REQUEST
        ));
        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.setName(user.getName());
        profileResponse.setEmail(user.getEmail());
        profileResponse.setRole(user.getRole());
        return profileResponse;
    }
}
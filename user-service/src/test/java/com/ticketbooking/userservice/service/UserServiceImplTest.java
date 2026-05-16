package com.ticketbooking.userservice.service;

import org.junit.jupiter.api.Test;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    // Common test data
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User existingUser;

    @BeforeEach
    void setUp() {
        // Har test se pehle ye data ready hoga
        registerRequest = new RegisterRequest();
        registerRequest.setName("Amit");
        registerRequest.setEmail("amit@test.com");
        registerRequest.setPassword("123456");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("amit@test.com");
        loginRequest.setPassword("123456");

        existingUser = new User();
        existingUser.setName("Amit");
        existingUser.setEmail("amit@test.com");
        existingUser.setPassword("encrypted123");
        existingUser.setRole(Role.USER);
    }

    // ═══════════════════════════════════════
    // REGISTER TESTS
    // ═══════════════════════════════════════

    @Test
    void register_success() {
        // GIVEN
        when(userRepository.existsByEmail("amit@test.com"))
                .thenReturn(false);
        when(passwordEncoder.encode("123456"))
                .thenReturn("encrypted123");
        when(userRepository.save(any(User.class)))
                .thenAnswer(i -> i.getArgument(0));

        // WHEN
        userService.register(registerRequest);

        // THEN
        // 1. save ek baar hua?
        verify(userRepository, times(1))
                .save(any(User.class));
        // 2. password encrypt hua?
        verify(passwordEncoder, times(1))
                .encode("123456");
    }

    @Test
    void register_passwordIsEncryptedBeforeSave() {
        // GIVEN
        when(userRepository.existsByEmail(any()))
                .thenReturn(false);
        when(passwordEncoder.encode("123456"))
                .thenReturn("encrypted123");

        // Save ke waqt user capture karo
        when(userRepository.save(any(User.class)))
                .thenAnswer(i -> {
                    User savedUser = i.getArgument(0);
                    // Check — plain text save nahi hua
                    assertNotEquals("123456", savedUser.getPassword());
                    assertEquals("encrypted123", savedUser.getPassword());
                    return savedUser;
                });

        // WHEN
        userService.register(registerRequest);

        // THEN
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_roleIsSetToUser() {
        // GIVEN
        when(userRepository.existsByEmail(any()))
                .thenReturn(false);
        when(passwordEncoder.encode(any()))
                .thenReturn("encrypted123");

        // Save ke waqt role check karo
        when(userRepository.save(any(User.class)))
                .thenAnswer(i -> {
                    User savedUser = i.getArgument(0);
                    assertEquals(Role.USER, savedUser.getRole());
                    return savedUser;
                });

        // WHEN
        userService.register(registerRequest);

        // THEN
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_whenEmailExists_throwException() {
        // GIVEN
        when(userRepository.existsByEmail("amit@test.com"))
                .thenReturn(true);

        // WHEN + THEN
        CustomException ex = assertThrows(
                CustomException.class,
                () -> userService.register(registerRequest)
        );

        // Sahi error code aaya?
        assertEquals(
                ErrorCodeEnum.EMAIL_ALREADY_EXISTS.getCode(),
                ex.getErrorCode()
        );
        // Sahi HTTP status aaya?
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
    }

    @Test
    void register_whenEmailExists_userNotSaved() {
        // GIVEN
        when(userRepository.existsByEmail("amit@test.com"))
                .thenReturn(true);

        // WHEN
        assertThrows(
                CustomException.class,
                () -> userService.register(registerRequest)
        );

        // THEN — save kabhi nahi hua
        verify(userRepository, never())
                .save(any(User.class));
    }

    // ═══════════════════════════════════════
    // LOGIN TESTS
    // ═══════════════════════════════════════

    @Test
    void login_success() {
        // GIVEN
        when(userRepository.findByEmail("amit@test.com"))
                .thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("123456", "encrypted123"))
                .thenReturn(true);
        when(jwtUtil.generateToken("amit@test.com", "USER"))
                .thenReturn("fake-jwt-token");

        // WHEN
        AuthResponse response = userService.login(loginRequest);

        // THEN
        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getToken());
        assertEquals("Amit", response.getName());
        assertEquals("amit@test.com", response.getEmail());
    }

    @Test
    void login_tokenIsGenerated() {
        // GIVEN
        when(userRepository.findByEmail("amit@test.com"))
                .thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(any(), any()))
                .thenReturn(true);
        when(jwtUtil.generateToken("amit@test.com", "USER"))
                .thenReturn("fake-jwt-token");

        // WHEN
        userService.login(loginRequest);

        // THEN — token generate hua?
        verify(jwtUtil, times(1))
                .generateToken("amit@test.com", "USER");
    }

    @Test
    void login_whenEmailNotFound_throwException() {
        // GIVEN
        when(userRepository.findByEmail("amit@test.com"))
                .thenReturn(Optional.empty());

        // WHEN + THEN
        CustomException ex = assertThrows(
                CustomException.class,
                () -> userService.login(loginRequest)
        );

        assertEquals(
                ErrorCodeEnum.EMAIL_NOT_FOUND.getCode(),
                ex.getErrorCode()
        );
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
    }

    @Test
    void login_whenEmailNotFound_tokenNotGenerated() {
        // GIVEN
        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.empty());

        // WHEN
        assertThrows(
                CustomException.class,
                () -> userService.login(loginRequest)
        );

        // THEN — token generate nahi hona chahiye
        verify(jwtUtil, never())
                .generateToken(any(), any());
    }

    @Test
    void login_whenWrongPassword_throwException() {
        // GIVEN
        when(userRepository.findByEmail("amit@test.com"))
                .thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("123456", "encrypted123"))
                .thenReturn(false);

        // WHEN + THEN
        CustomException ex = assertThrows(
                CustomException.class,
                () -> userService.login(loginRequest)
        );

        assertEquals(
                ErrorCodeEnum.WRONG_PASSWORD.getCode(),
                ex.getErrorCode()
        );
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getHttpStatus());
    }

    @Test
    void login_whenWrongPassword_tokenNotGenerated() {
        // GIVEN
        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(any(), any()))
                .thenReturn(false);

        // WHEN
        assertThrows(
                CustomException.class,
                () -> userService.login(loginRequest)
        );

        // THEN — token generate nahi hona chahiye
        verify(jwtUtil, never())
                .generateToken(any(), any());
    }

    // ═══════════════════════════════════════
    // GET PROFILE TESTS
    // ═══════════════════════════════════════

    @Test
    void getProfile_success() {
        // GIVEN
        when(userRepository.findByEmail("amit@test.com"))
                .thenReturn(Optional.of(existingUser));

        // WHEN
        ProfileResponse response = userService.getProfile("amit@test.com");

        // THEN
        assertNotNull(response);
        assertEquals("Amit", response.getName());
        assertEquals("amit@test.com", response.getEmail());
        assertEquals(Role.USER, response.getRole());
    }

    @Test
    void getProfile_whenUserNotFound_throwException() {
        // GIVEN
        when(userRepository.findByEmail("notexist@test.com"))
                .thenReturn(Optional.empty());

        // WHEN + THEN
        CustomException ex = assertThrows(
                CustomException.class,
                () -> userService.getProfile("notexist@test.com")
        );

        assertEquals(
                ErrorCodeEnum.USER_NOT_FOUND.getCode(),
                ex.getErrorCode()
        );
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
    }

    @Test
    void getProfile_allFieldsMapped() {
        // GIVEN
        when(userRepository.findByEmail("amit@test.com"))
                .thenReturn(Optional.of(existingUser));

        // WHEN
        ProfileResponse response = userService.getProfile("amit@test.com");

        // THEN — saare fields map hue?
        assertNotNull(response.getName());
        assertNotNull(response.getEmail());
        assertNotNull(response.getRole());
    }
}
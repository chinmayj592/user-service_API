package com.chinmay.user_service_api.service;

import com.chinmay.user_service_api.dto.UserRequestDTO;
import com.chinmay.user_service_api.dto.UserResponseDTO;
import com.chinmay.user_service_api.entity.User;
import com.chinmay.user_service_api.exception.ResourceNotFoundException;
import com.chinmay.user_service_api.mapper.UserMapper;
import com.chinmay.user_service_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequestDTO userRequestDTO;
    private User user;

    @BeforeEach
    void setUp() {
        userRequestDTO = new UserRequestDTO(
                "John Doe",
                "john@example.com",
                "9876543210",
                "9876543211",
                "123456789012",
                "ABCDE1234F",
                LocalDate.of(1990, 1, 1),
                "Delhi",
                "123 Main St",
                "456 Old St"
        );

        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPrimaryMobile("9876543210");
        user.setSecondaryMobile("9876543211");
        user.setAadhaar("123456789012");
        user.setPan("ABCDE1234F");
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));
        user.setPlaceOfBirth("Delhi");
        user.setCurrentAddress("123 Main St");
        user.setPermanentAddress("456 Old St");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setIsDeleted(false);
    }

    @Test
    @DisplayName("Should create user successfully when all fields are valid")
    void testCreateUserSuccess() {
        when(userRepository.existsByEmail(userRequestDTO.email())).thenReturn(false);
        when(userRepository.existsByAadhaar(userRequestDTO.aadhaar())).thenReturn(false);
        when(userRepository.existsByPan(userRequestDTO.pan())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO response = userService.create(userRequestDTO);

        assertNotNull(response);
        assertEquals(user.getId(), response.id());
        assertEquals(user.getName(), response.name());
        assertEquals(user.getEmail(), response.email());
        verify(userRepository).existsByEmail(userRequestDTO.email());
        verify(userRepository).existsByAadhaar(userRequestDTO.aadhaar());
        verify(userRepository).existsByPan(userRequestDTO.pan());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void testCreateUserEmailAlreadyExists() {
        when(userRepository.existsByEmail(userRequestDTO.email())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.create(userRequestDTO),
                "Email already exists");
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when aadhaar already exists")
    void testCreateUserAadhaarAlreadyExists() {
        when(userRepository.existsByEmail(userRequestDTO.email())).thenReturn(false);
        when(userRepository.existsByAadhaar(userRequestDTO.aadhaar())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.create(userRequestDTO),
                "Aadhaar already exists");
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when PAN already exists")
    void testCreateUserPanAlreadyExists() {
        when(userRepository.existsByEmail(userRequestDTO.email())).thenReturn(false);
        when(userRepository.existsByAadhaar(userRequestDTO.aadhaar())).thenReturn(false);
        when(userRepository.existsByPan(userRequestDTO.pan())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.create(userRequestDTO),
                "PAN already exists");
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update user successfully when user exists")
    void testUpdateUserSuccess() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO response = userService.update(userId, userRequestDTO);

        assertNotNull(response);
        assertEquals(user.getId(), response.id());
        verify(userRepository).findById(userId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existing user")
    void testUpdateUserNotFound() {
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.update(userId, userRequestDTO),
                "User not found");
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should retrieve all users with pagination")
    void testGetAllUsersSuccess() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(user), pageable, 1);
        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<UserResponseDTO> response = userService.getAll(pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(user.getId(), response.getContent().get(0).id());
        verify(userRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Should return empty page when no users exist")
    void testGetAllUsersEmpty() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(), pageable, 0);
        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<UserResponseDTO> response = userService.getAll(pageable);

        assertNotNull(response);
        assertEquals(0, response.getTotalElements());
        verify(userRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Should delete user successfully when user exists")
    void testDeleteUserSuccess() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        assertDoesNotThrow(() -> userService.delete(userId));
        verify(userRepository).findById(userId);
        verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existing user")
    void testDeleteUserNotFound() {
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.delete(userId),
                "User not found");
        verify(userRepository, never()).delete(any());
    }
}


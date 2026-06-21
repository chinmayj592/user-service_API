package com.chinmay.user_service_api.controller;

import com.chinmay.user_service_api.dto.UserRequestDTO;
import com.chinmay.user_service_api.dto.UserResponseDTO;
import com.chinmay.user_service_api.exception.ResourceNotFoundException;
import com.chinmay.user_service_api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@DisplayName("UserController Unit Tests")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;

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

        userResponseDTO = new UserResponseDTO(
                1L,
                "John Doe",
                "john@example.com",
                "9876543210",
                "9876543211",
                "123456789012",
                "ABCDE1234F",
                LocalDate.of(1990, 1, 1),
                "Delhi",
                "123 Main St",
                "456 Old St",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("Test endpoint is working")
    void testControllerTest() throws Exception {
        mockMvc.perform(get("/api/v1/users/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("Controller is working!"));
    }

    @Test
    @DisplayName("Should create user with valid data - returns 201 CREATED")
    void testCreateUserSuccess() throws Exception {
        when(userService.create(any(UserRequestDTO.class))).thenReturn(userResponseDTO);

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(userService).create(any(UserRequestDTO.class));
    }

    @Test
    @DisplayName("Should fail to create user with invalid email")
    void testCreateUserInvalidEmail() throws Exception {
        UserRequestDTO invalidDTO = new UserRequestDTO(
                "John Doe",
                "invalid-email",
                "9876543210",
                "9876543211",
                "123456789012",
                "ABCDE1234F",
                LocalDate.of(1990, 1, 1),
                "Delhi",
                "123 Main St",
                "456 Old St"
        );

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).create(any());
    }

    @Test
    @DisplayName("Should fail to create user with missing required fields")
    void testCreateUserMissingFields() throws Exception {
        String invalidJSON = "{\"email\": \"john@example.com\"}";

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJSON))
                .andExpect(status().isBadRequest());

        verify(userService, never()).create(any());
    }

    @Test
    @DisplayName("Should update user successfully - returns 200 OK")
    void testUpdateUserSuccess() throws Exception {
        Long userId = 1L;
        when(userService.update(eq(userId), any(UserRequestDTO.class))).thenReturn(userResponseDTO);

        mockMvc.perform(put("/api/v1/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(userService).update(eq(userId), any(UserRequestDTO.class));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existing user")
    void testUpdateUserNotFound() throws Exception {
        Long userId = 999L;
        when(userService.update(eq(userId), any(UserRequestDTO.class)))
                .thenThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(put("/api/v1/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should retrieve all users with pagination - returns 200 OK")
    void testGetAllUsersSuccess() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserResponseDTO> userPage = new PageImpl<>(List.of(userResponseDTO), pageable, 1);
        when(userService.getAll(any(Pageable.class))).thenReturn(userPage);

        mockMvc.perform(get("/api/v1/users")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(userService).getAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should return empty page when no users exist")
    void testGetAllUsersEmpty() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserResponseDTO> emptyPage = new PageImpl<>(List.of(), pageable, 0);
        when(userService.getAll(any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/users")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    @DisplayName("Should delete user successfully - returns 204 NO CONTENT")
    void testDeleteUserSuccess() throws Exception {
        Long userId = 1L;
        doNothing().when(userService).delete(userId);

        mockMvc.perform(delete("/api/v1/users/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userService).delete(userId);
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existing user")
    void testDeleteUserNotFound() throws Exception {
        Long userId = 999L;
        doThrow(new ResourceNotFoundException("User not found")).when(userService).delete(userId);

        mockMvc.perform(delete("/api/v1/users/{id}", userId))
                .andExpect(status().isNotFound());
    }
}


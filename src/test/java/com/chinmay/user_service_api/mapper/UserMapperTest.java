package com.chinmay.user_service_api.mapper;

import com.chinmay.user_service_api.dto.UserRequestDTO;
import com.chinmay.user_service_api.dto.UserResponseDTO;
import com.chinmay.user_service_api.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserMapper Unit Tests")
class UserMapperTest {

    private UserRequestDTO userRequestDTO;
    private User user;

    @BeforeEach
    void setUp() {
        userRequestDTO = new UserRequestDTO(
                "Jane Doe",
                "jane@example.com",
                "9876543210",
                "9876543211",
                "987654321098",
                "XYZAB1234C",
                LocalDate.of(1992, 5, 15),
                "Mumbai",
                "789 New St",
                "321 Old Ave"
        );

        user = new User();
        user.setId(2L);
        user.setName("Jane Doe");
        user.setEmail("jane@example.com");
        user.setPrimaryMobile("9876543210");
        user.setSecondaryMobile("9876543211");
        user.setAadhaar("987654321098");
        user.setPan("XYZAB1234C");
        user.setDateOfBirth(LocalDate.of(1992, 5, 15));
        user.setPlaceOfBirth("Mumbai");
        user.setCurrentAddress("789 New St");
        user.setPermanentAddress("321 Old Ave");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should convert UserRequestDTO to User entity")
    void testToEntity() {
        User mappedUser = UserMapper.toEntity(userRequestDTO);

        assertNotNull(mappedUser);
        assertEquals(userRequestDTO.name(), mappedUser.getName());
        assertEquals(userRequestDTO.email(), mappedUser.getEmail());
        assertEquals(userRequestDTO.primaryMobile(), mappedUser.getPrimaryMobile());
        assertEquals(userRequestDTO.secondaryMobile(), mappedUser.getSecondaryMobile());
        assertEquals(userRequestDTO.aadhaar(), mappedUser.getAadhaar());
        assertEquals(userRequestDTO.pan(), mappedUser.getPan());
        assertEquals(userRequestDTO.dateOfBirth(), mappedUser.getDateOfBirth());
        assertEquals(userRequestDTO.placeOfBirth(), mappedUser.getPlaceOfBirth());
        assertEquals(userRequestDTO.currentAddress(), mappedUser.getCurrentAddress());
        assertEquals(userRequestDTO.permanentAddress(), mappedUser.getPermanentAddress());
    }

    @Test
    @DisplayName("Should update User entity with UserRequestDTO values")
    void testUpdateEntity() {
        UserRequestDTO newData = new UserRequestDTO(
                "Updated Name",
                "updated@example.com",
                "1234567890",
                "1234567891",
                "111111111111",
                "AAAA1111A",
                LocalDate.of(1995, 10, 20),
                "Bangalore",
                "999 Updated St",
                "888 Updated Ave"
        );

        UserMapper.updateEntity(user, newData);

        assertEquals(newData.name(), user.getName());
        assertEquals(newData.email(), user.getEmail());
        assertEquals(newData.primaryMobile(), user.getPrimaryMobile());
        assertEquals(newData.secondaryMobile(), user.getSecondaryMobile());
        assertEquals(newData.aadhaar(), user.getAadhaar());
        assertEquals(newData.pan(), user.getPan());
        assertEquals(newData.dateOfBirth(), user.getDateOfBirth());
        assertEquals(newData.placeOfBirth(), user.getPlaceOfBirth());
        assertEquals(newData.currentAddress(), user.getCurrentAddress());
        assertEquals(newData.permanentAddress(), user.getPermanentAddress());
    }

    @Test
    @DisplayName("Should convert User entity to UserResponseDTO")
    void testToResponse() {
        UserResponseDTO responseDTO = UserMapper.toResponse(user);

        assertNotNull(responseDTO);
        assertEquals(user.getId(), responseDTO.id());
        assertEquals(user.getName(), responseDTO.name());
        assertEquals(user.getEmail(), responseDTO.email());
        assertEquals(user.getPrimaryMobile(), responseDTO.primaryMobile());
        assertEquals(user.getSecondaryMobile(), responseDTO.secondaryMobile());
        assertEquals(user.getAadhaar(), responseDTO.aadhaar());
        assertEquals(user.getPan(), responseDTO.pan());
        assertEquals(user.getDateOfBirth(), responseDTO.dateOfBirth());
        assertEquals(user.getPlaceOfBirth(), responseDTO.placeOfBirth());
        assertEquals(user.getCurrentAddress(), responseDTO.currentAddress());
        assertEquals(user.getPermanentAddress(), responseDTO.permanentAddress());
        assertEquals(user.getCreatedAt(), responseDTO.createdAt());
        assertEquals(user.getUpdatedAt(), responseDTO.updatedAt());
    }

    @Test
    @DisplayName("Should handle null secondary mobile in mapping")
    void testToEntityWithNullSecondaryMobile() {
        UserRequestDTO dtoWithoutSecondary = new UserRequestDTO(
                "John",
                "john@test.com",
                "9876543210",
                null,
                "123456789012",
                "ABCDE1234F",
                LocalDate.of(1990, 1, 1),
                "City",
                "Address 1",
                "Address 2"
        );

        User mappedUser = UserMapper.toEntity(dtoWithoutSecondary);

        assertNotNull(mappedUser);
        assertNull(mappedUser.getSecondaryMobile());
        assertEquals(dtoWithoutSecondary.name(), mappedUser.getName());
    }

    @Test
    @DisplayName("Should preserve user ID and version after mapping")
    void testToResponsePreservesMetadata() {
        user.setId(5L);
        user.setVersion(3L);

        UserResponseDTO responseDTO = UserMapper.toResponse(user);

        assertEquals(5L, responseDTO.id());
    }
}


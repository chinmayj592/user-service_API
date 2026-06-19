package com.chinmay.user_service_api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String primaryMobile,
        String secondaryMobile,
        String aadhaar,
        String pan,
        LocalDate dateOfBirth,
        String placeOfBirth,
        String currentAddress,
        String permanentAddress,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
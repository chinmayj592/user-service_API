package com.chinmay.user_service_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserRequestDTO(

        @NotBlank String name,

        @Email @NotBlank String email,

        @NotBlank String primaryMobile,

        String secondaryMobile,

        @NotBlank String aadhaar,

        @NotBlank String pan,

        @NotNull LocalDate dateOfBirth,

        String placeOfBirth,

        @NotBlank String currentAddress,

        @NotBlank String permanentAddress
) {}
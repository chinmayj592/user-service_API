package com.chinmay.user_service_api.mapper;

import com.chinmay.userservice.dto.UserRequestDTO;
import com.chinmay.userservice.dto.UserResponseDTO;
import com.chinmay.userservice.entity.User;

public class UserMapper {

    private UserMapper() {}

    public static User toEntity(UserRequestDTO dto) {
        User user = new User();
        updateEntity(user, dto);
        return user;
    }

    public static void updateEntity(User user, UserRequestDTO dto) {
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPrimaryMobile(dto.primaryMobile());
        user.setSecondaryMobile(dto.secondaryMobile());
        user.setAadhaar(dto.aadhaar());
        user.setPan(dto.pan());
        user.setDateOfBirth(dto.dateOfBirth());
        user.setPlaceOfBirth(dto.placeOfBirth());
        user.setCurrentAddress(dto.currentAddress());
        user.setPermanentAddress(dto.permanentAddress());
    }


}
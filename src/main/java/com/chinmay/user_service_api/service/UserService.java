package com.chinmay.user_service_api.service;

import com.chinmay.userservice.dto.UserRequestDTO;
import com.chinmay.userservice.dto.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResponseDTO create(UserRequestDTO dto);

    UserResponseDTO update(Long id, UserRequestDTO dto);

    Page<UserResponseDTO> getAll(Pageable pageable);

    void delete(Long id);
}
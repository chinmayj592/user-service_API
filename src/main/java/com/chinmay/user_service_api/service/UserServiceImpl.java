package com.chinmay.user_service_api.service;

import com.chinmay.user_service_api.dto.UserRequestDTO;
import com.chinmay.user_service_api.dto.UserResponseDTO;
import com.chinmay.user_service_api.entity.User;
import com.chinmay.user_service_api.exception.ResourceNotFoundException;
import com.chinmay.user_service_api.mapper.UserMapper;
import com.chinmay.user_service_api.repository.UserRepository;
import com.chinmay.user_service_api.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserResponseDTO create(UserRequestDTO dto) {

        if (repository.existsByEmail(dto.email()))
            throw new IllegalArgumentException("Email already exists");

        if (repository.existsByAadhaar(dto.aadhaar()))
            throw new IllegalArgumentException("Aadhaar already exists");

        if (repository.existsByPan(dto.pan()))
            throw new IllegalArgumentException("PAN already exists");

        User user = UserMapper.toEntity(dto);
        return UserMapper.toResponse(repository.save(user));
    }

    @Override
    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserMapper.updateEntity(user, dto);
        return UserMapper.toResponse(repository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(UserMapper::toResponse);
    }

    @Override
    public void delete(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        repository.delete(user);
    }
}
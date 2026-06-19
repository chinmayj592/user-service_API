package com.chinmay.user_service_api.controller;

import com.chinmay.user_service_api.dto.UserRequestDTO;
import com.chinmay.user_service_api.dto.UserResponseDTO;
import com.chinmay.user_service_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
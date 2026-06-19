package com.chinmay.user_service_api.repository;

import com.chinmay.user_service_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByAadhaar(String aadhaar);
    boolean existsByPan(String pan);
}
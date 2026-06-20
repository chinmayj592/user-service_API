package com.chinmay.user_service_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "aadhaar"),
        @UniqueConstraint(columnNames = "pan")
    }
)
@Getter
@Setter
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ? AND version = ?")

@Where(clause = "is_deleted = false")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String primaryMobile;

    private String secondaryMobile;

    @Column(nullable = false, length = 12)
    private String aadhaar;

    @Column(nullable = false, length = 10)
    private String pan;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    private String placeOfBirth;

    @Column(nullable = false)
    private String currentAddress;

    @Column(nullable = false)
    private String permanentAddress;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
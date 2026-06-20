CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       primary_mobile VARCHAR(20) NOT NULL,
                       secondary_mobile VARCHAR(20),
                       aadhaar VARCHAR(12) NOT NULL UNIQUE,
                       pan VARCHAR(10) NOT NULL UNIQUE,
                       date_of_birth DATE NOT NULL,
                       place_of_birth VARCHAR(255),
                       current_address TEXT NOT NULL,
                       permanent_address TEXT NOT NULL,
                       is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                       created_at DATETIME NOT NULL,
                       updated_at DATETIME NOT NULL,
                       version BIGINT DEFAULT 0
);

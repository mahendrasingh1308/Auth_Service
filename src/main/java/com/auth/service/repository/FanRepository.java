package com.auth.service.repository;

import com.auth.service.entity.Fan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FanRepository extends JpaRepository<Fan, Long> {
    Optional<Fan> findByEmail(String email);
    Optional<Fan> findByUsername(String username);
    Optional<Fan> findByPhone(String phone);
}

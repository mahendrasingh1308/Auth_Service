package com.auth.service.repository;

import com.auth.service.entity.Creator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreatorRepository extends JpaRepository<Creator, Long> {
    Optional<Creator> findByEmail(String email);
    Optional<Creator> findByUsername(String username);
    Optional<Creator> findByMobile(String mobile);
}

package com.auth.service.repository;

import com.auth.service.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {

    Optional<UserCredential> findByEmail(String email);

    Optional<UserCredential> findByPhone(String phone);

    Optional<UserCredential> findByUuid(String uuid);

    boolean existsByEmail(String email);


    boolean existsByUsername(String username);
}

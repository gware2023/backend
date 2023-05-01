package com.dev.gware.user.repository;

import com.dev.gware.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findById (Long id);

    Users findByLoginId(String LoginId);
}

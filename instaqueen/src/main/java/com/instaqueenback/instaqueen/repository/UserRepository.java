package com.instaqueenback.instaqueen.repository;

import com.instaqueenback.instaqueen.entity.User;
import com.instaqueenback.instaqueen.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    long countByRole(Role role);
    Page<User> findByRole(Role role, Pageable pageable);
}

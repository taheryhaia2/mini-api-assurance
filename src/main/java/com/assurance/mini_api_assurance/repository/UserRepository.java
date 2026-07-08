package com.assurance.mini_api_assurance.repository;

import com.assurance.mini_api_assurance.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
}

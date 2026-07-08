package com.assurance.mini_api_assurance.repository;

import com.assurance.mini_api_assurance.domain.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
}

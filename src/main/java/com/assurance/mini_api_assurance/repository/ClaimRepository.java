package com.assurance.mini_api_assurance.repository;

import com.assurance.mini_api_assurance.domain.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findByContractId(Long contractId);
}

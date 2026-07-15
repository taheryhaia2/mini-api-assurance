package com.assurance.mini_api_assurance.repository;

import com.assurance.mini_api_assurance.domain.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    boolean existsByClientId(Long clientId);
}

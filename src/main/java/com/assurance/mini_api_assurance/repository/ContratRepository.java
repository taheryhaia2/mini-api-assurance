package com.assurance.mini_api_assurance.repository;

import com.assurance.mini_api_assurance.domain.Contrat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContratRepository extends JpaRepository<Contrat, Long> {
}
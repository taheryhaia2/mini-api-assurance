package com.assurance.mini_api_assurance.repository;

import com.assurance.mini_api_assurance.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByCin(String cin);

    // Soft delete : on ne liste que les clients actifs
    List<Client> findByActiveTrue();
}
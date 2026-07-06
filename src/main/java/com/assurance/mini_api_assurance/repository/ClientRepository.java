package com.assurance.mini_api_assurance.repository;

import com.assurance.mini_api_assurance.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
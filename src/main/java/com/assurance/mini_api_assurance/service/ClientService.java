package com.assurance.mini_api_assurance.service;

import com.assurance.mini_api_assurance.domain.Client;
import com.assurance.mini_api_assurance.dto.ClientCreateDto;
import com.assurance.mini_api_assurance.dto.ClientResponseDto;
import com.assurance.mini_api_assurance.dto.ClientUpdateDto;
import com.assurance.mini_api_assurance.exception.BusinessRuleException;
import com.assurance.mini_api_assurance.exception.NotFoundException;
import com.assurance.mini_api_assurance.mapper.ClientMapper;
import com.assurance.mini_api_assurance.repository.ClientRepository;
import com.assurance.mini_api_assurance.repository.ContractRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ContractRepository contractRepository;

    public ClientService(ClientRepository clientRepository, ContractRepository contractRepository) {
        this.clientRepository = clientRepository;
        this.contractRepository = contractRepository;
    }

    @Transactional
    public ClientResponseDto createClient(ClientCreateDto dto) {
        // Contrôle CIN unique
        if (clientRepository.existsByCin(dto.cin())) {
            throw new BusinessRuleException("Un client avec ce CIN existe déjà.");
        }

        // Âge minimum 18 ans
        LocalDate today = LocalDate.now();
        if (dto.birthDate() == null || dto.birthDate().isAfter(today.minusYears(18))) {
            throw new BusinessRuleException("Le client doit avoir au moins 18 ans.");
        }

        // 1. Convert DTO -> Entity
        Client client = ClientMapper.toEntity(dto);

        // Business rules forced by the server
        client.setCreatedAt(LocalDate.now());
        client.setActive(true);

        // 2. Save to database via Repository
        Client savedClient = clientRepository.save(client);

        // 3. Convert Entity -> Response DTO
        return ClientMapper.toDto(savedClient);
    }

    @Transactional(readOnly = true)
    public List<ClientResponseDto> listClients() {
        return clientRepository.findByActiveTrue().stream()
                .map(ClientMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClientResponseDto getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found with id: " + id));

        return ClientMapper.toDto(client);
    }

    @Transactional
    public ClientResponseDto updateClient(Long id, ClientUpdateDto dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found with ID: " + id));

        // On n'autorise pas la modification d'un client déjà archivé
        if (!client.isActive()) {
            throw new BusinessRuleException("Impossible de modifier un client archivé.");
        }

        ClientMapper.updateEntity(client, dto);
        Client saved = clientRepository.save(client);
        return ClientMapper.toDto(saved);
    }

    @Transactional
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found with ID: " + id));

        // Soft delete : on archive, on n'efface pas
        client.setActive(false);
        clientRepository.save(client);
    }
}
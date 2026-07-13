package com.assurance.mini_api_assurance.service;

import com.assurance.mini_api_assurance.domain.Client;
import com.assurance.mini_api_assurance.dto.ClientCreateDto;
import com.assurance.mini_api_assurance.dto.ClientResponseDto;
import com.assurance.mini_api_assurance.dto.ClientUpdateDto;
import com.assurance.mini_api_assurance.mapper.ClientMapper;
import com.assurance.mini_api_assurance.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.assurance.mini_api_assurance.exception.NotFoundException;
@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    public ClientResponseDto createClient(ClientCreateDto dto) {
        // 1. Convert DTO -> Entity
        Client client = ClientMapper.toEntity(dto);

        // Business rule: creation date is forced by the server, not by the client
        client.setCreatedAt(java.time.LocalDate.now());

        // 2. Save to database via Repository
        Client savedClient = clientRepository.save(client);

        // 3. Convert Entity -> Response DTO
        return ClientMapper.toDto(savedClient);
    }

    @Transactional(readOnly = true)
    public List<ClientResponseDto> listClients() {
        return clientRepository.findAll().stream()
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
    public ClientResponseDto updateClient(Long id, ClientUpdateDto dto){
        Client client=clientRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Client not found with ID: " + id));
        ClientMapper.updateEntity(client,dto);
        Client saved =clientRepository.save(client);
        return ClientMapper.toDto(saved);
    }

}

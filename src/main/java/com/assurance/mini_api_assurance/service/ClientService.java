package com.assurance.mini_api_assurance.service;

import com.assurance.mini_api_assurance.domain.Client;
import com.assurance.mini_api_assurance.dto.ClientCreateDto;
import com.assurance.mini_api_assurance.dto.ClientResponseDto;
import com.assurance.mini_api_assurance.mapper.ClientMapper;
import com.assurance.mini_api_assurance.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    // Injection par constructeur (ne pas utiliser @Autowired sur le champ)
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    public ClientResponseDto creerClient(ClientCreateDto dto) {
        // 1. Conversion DTO -> Entité
        Client client = ClientMapper.toEntity(dto);

        // Règle métier : la date de création est forcée par le serveur, pas par le client
        client.setDateCreation(java.time.LocalDate.now());

        // 2. Sauvegarde en base via le Repository
        Client savedClient = clientRepository.save(client);

        // 3. Conversion Entité -> DTO de réponse
        return ClientMapper.toDto(savedClient);
    }
}
package com.assurance.mini_api_assurance.service;

import com.assurance.mini_api_assurance.domain.Client;
import com.assurance.mini_api_assurance.dto.ClientCreateDto;
import com.assurance.mini_api_assurance.dto.ClientResponseDto;
import com.assurance.mini_api_assurance.mapper.ClientMapper;
import com.assurance.mini_api_assurance.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
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
    @Transactional(readOnly = true)
    public List<ClientResponseDto>listerClients(){
        return clientRepository.findAll().stream()
                .map(ClientMapper::toDto)
                .toList();
    }
    @Transactional(readOnly = true)
    public ClientResponseDto getClientParId(Long id) {
        // findById renvoie un Optional. Soit y'a le client, soit y'a rien.
        // orElseThrow : s'il n'y a rien, on lève une exception immédiatement.
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id : " + id));

        return ClientMapper.toDto(client);
    }

}
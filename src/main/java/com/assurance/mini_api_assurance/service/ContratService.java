package com.assurance.mini_api_assurance.service;

import com.assurance.mini_api_assurance.domain.Client;
import com.assurance.mini_api_assurance.domain.Contrat;
import com.assurance.mini_api_assurance.domain.StatutContrat;
import com.assurance.mini_api_assurance.dto.ContratCreateDto;
import com.assurance.mini_api_assurance.dto.ContratResponseDto;
import com.assurance.mini_api_assurance.mapper.ContratMapper;
import com.assurance.mini_api_assurance.repository.ClientRepository;
import com.assurance.mini_api_assurance.repository.ContratRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContratService {

    private final ContratRepository contratRepository;
    private final ClientRepository clientRepository;

    public ContratService(ContratRepository contratRepository, ClientRepository clientRepository) {
        this.contratRepository = contratRepository;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public ContratResponseDto creerContrat(ContratCreateDto dto) {
        // 1. Règle métier : La date de fin doit être après la date de début
        if (dto.dateFin().isBefore(dto.dateDebut())) {
            throw new RuntimeException("La date de fin ne peut pas être antérieure à la date de début");
        }

        // 2. Vérifier que le client existe
        Client client = clientRepository.findById(dto.clientId())
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id : " + dto.clientId()));

        // 3. Mapper et préparer le contrat
        Contrat contrat = ContratMapper.toEntity(dto);
        contrat.setClient(client); // On attache l'objet Client réel
        contrat.setStatut(StatutContrat.ACTIF); // Règle : nouveau contrat = ACTIF

        // 4. Sauvegarder
        Contrat savedContrat = contratRepository.save(contrat);

        // 5. Retourner la réponse
        return ContratMapper.toDto(savedContrat);
    }
    @Transactional(readOnly = true)
    public java.util.List<com.assurance.mini_api_assurance.dto.ContratResponseDto> listerContrats() {
        return contratRepository.findAll().stream()
                .map(contrat -> new ContratResponseDto(
                        contrat.getId(),
                        contrat.getClient().getId(),
                        contrat.getDateDebut(),
                        contrat.getDateFin(),
                        contrat.getStatut(),             // Statut d'abord
                        contrat.getMontantCouverture()   // Montant ensuite
                ))
                .toList();
    }
    @Transactional(readOnly = true)
    public com.assurance.mini_api_assurance.dto.ContratResponseDto obtenirContratParId(Long id){
        Contrat contrat=contratRepository.findById(id).orElseThrow(()->new RuntimeException());
        return new ContratResponseDto(
                contrat.getId(),
                contrat.getClient().getId(),
                contrat.getDateDebut(),
                contrat.getDateFin(),
                contrat.getStatut(),             // Statut d'abord
                contrat.getMontantCouverture()   // Montant ensuite
        );
    }
}
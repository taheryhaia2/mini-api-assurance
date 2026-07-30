package com.assurance.mini_api_assurance.service;

import com.assurance.mini_api_assurance.domain.Claim;
import com.assurance.mini_api_assurance.domain.ClaimStatus;
import com.assurance.mini_api_assurance.domain.Contract;
import com.assurance.mini_api_assurance.domain.ContractStatus;
import com.assurance.mini_api_assurance.dto.ClaimCreateDto;
import com.assurance.mini_api_assurance.dto.ClaimResponseDto;
import com.assurance.mini_api_assurance.exception.BusinessRuleException;
import com.assurance.mini_api_assurance.exception.NotFoundException;
import com.assurance.mini_api_assurance.mapper.ClaimMapper;
import com.assurance.mini_api_assurance.repository.ClaimRepository;
import com.assurance.mini_api_assurance.repository.ContractRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final ContractRepository contractRepository;

    // Le dossier où on va stocker les fichiers
    private final Path rootLocation = Paths.get("uploads");

    public ClaimService(ClaimRepository claimRepository, ContractRepository contractRepository) {
        this.claimRepository = claimRepository;
        this.contractRepository = contractRepository;

        // Création du dossier uploads au démarrage du service s'il n'existe pas
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de créer le dossier de stockage.", e);
        }
    }

    @Transactional
    public ClaimResponseDto createClaim(Long contractId, ClaimCreateDto dto, MultipartFile file) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new NotFoundException("Contract not found with ID: " + contractId));

        if (contract.getStatus() != ContractStatus.ACTIVE) {
            throw new BusinessRuleException("Cannot file a claim: Contract is not ACTIVE");
        }

        if (dto.claimDate().isBefore(contract.getStartDate()) || dto.claimDate().isAfter(contract.getEndDate())) {
            throw new BusinessRuleException("Claim date must be within the contract coverage period");
        }

        Claim claim = new Claim();
        claim.setContract(contract);
        claim.setDescription(dto.description());
        claim.setClaimDate(dto.claimDate());
        claim.setDeclarationDate(LocalDate.now());
        claim.setEstimatedAmount(dto.estimatedAmount());
        claim.setReimbursedAmount(BigDecimal.ZERO);
        claim.setStatus(ClaimStatus.SUBMITTED);
        claim.setClaimNumber(generateClaimNumber());

        // --- GESTION DU FICHIER ---
        if (file != null && !file.isEmpty()) {
            try {
                // On garde l'extension du fichier original (ex: .pdf)
                String originalFilename = file.getOriginalFilename();
                String extension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }

                // On crée un nom unique pour éviter d'écraser un autre fichier
                String uniqueFileName = UUID.randomUUID().toString() + extension;
                Path destinationFile = this.rootLocation.resolve(uniqueFileName);

                // On copie le fichier sur le disque
                Files.copy(file.getInputStream(), destinationFile);

                // On sauvegarde le chemin dans notre entité
                claim.setDocumentPath(destinationFile.toString());

            } catch (IOException e) {
                throw new RuntimeException("Erreur lors de la sauvegarde du document.", e);
            }
        }
        // --------------------------

        Claim saved = claimRepository.save(claim);
        return ClaimMapper.toDto(saved);
    }

    private String generateClaimNumber() {
        return "CL-" + Year.now().getValue() + "-" + (System.currentTimeMillis() % 100000);
    }

    @Transactional(readOnly = true)
    public List<ClaimResponseDto> getClaimsByContractId(Long contractId){
        if (!contractRepository.existsById(contractId)) {
            throw new NotFoundException("Contract not found with ID: " + contractId);
        }
        List<Claim> claims = claimRepository.findByContractId(contractId);
        return claims.stream()
                .map(ClaimMapper::toDto)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<ClaimResponseDto> getAllClaims() {
        return claimRepository.findAll().stream()
                .map(ClaimMapper::toDto)
                .toList();
    }
    @Transactional(readOnly = true)
    public ClaimResponseDto getClaimById(Long id) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Claim not found with ID: " + id));
        return ClaimMapper.toDto(claim);
    }
}
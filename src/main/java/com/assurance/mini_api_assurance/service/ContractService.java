package com.assurance.mini_api_assurance.service;

import com.assurance.mini_api_assurance.domain.Client;
import com.assurance.mini_api_assurance.domain.Contract;
import com.assurance.mini_api_assurance.domain.ContractStatus;
import com.assurance.mini_api_assurance.dto.ContractCreateDto;
import com.assurance.mini_api_assurance.dto.ContractResponseDto;
import com.assurance.mini_api_assurance.exception.BusinessRuleException;
import com.assurance.mini_api_assurance.exception.NotFoundException;
import com.assurance.mini_api_assurance.mapper.ContractMapper;
import com.assurance.mini_api_assurance.repository.ClientRepository;
import com.assurance.mini_api_assurance.repository.ContractRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final ClientRepository clientRepository;

    public ContractService(ContractRepository contractRepository,
                           ClientRepository clientRepository) {
        this.contractRepository = contractRepository;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public ContractResponseDto createContract(ContractCreateDto dto) {
        Client client = clientRepository.findById(dto.clientId())
                .orElseThrow(() -> new NotFoundException("Client not found with ID: " + dto.clientId()));

        if (dto.endDate().isBefore(dto.startDate())) {
            throw new BusinessRuleException("End date must be after start date");
        }

        Contract contract = new Contract();
        contract.setClient(client);
        contract.setType(dto.type());
        contract.setStartDate(dto.startDate());
        contract.setEndDate(dto.endDate());
        contract.setCoverageAmount(dto.coverageAmount());
        contract.setPremiumAmount(dto.premiumAmount());
        contract.setStatus(ContractStatus.ACTIVE);
        contract.setPolicyNumber(generatePolicyNumber());

        Contract saved = contractRepository.save(contract);
        return ContractMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<ContractResponseDto> listContracts() {
        return contractRepository.findAll().stream()
                .map(ContractMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ContractResponseDto getContractById(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Contract not found with ID: " + id));

        return ContractMapper.toDto(contract);
    }

    private String generatePolicyNumber() {
        // Exemple: CT-2026-83421
        return "CT-" + Year.now().getValue() + "-" + (System.currentTimeMillis() % 100000);
    }
}
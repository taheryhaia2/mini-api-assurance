package com.assurance.mini_api_assurance.service;

import com.assurance.mini_api_assurance.domain.Client;
import com.assurance.mini_api_assurance.domain.Contract;
import com.assurance.mini_api_assurance.domain.ContractStatus;
import com.assurance.mini_api_assurance.dto.ContractCreateDto;
import com.assurance.mini_api_assurance.dto.ContractResponseDto;
import com.assurance.mini_api_assurance.mapper.ContractMapper;
import com.assurance.mini_api_assurance.repository.ClientRepository;
import com.assurance.mini_api_assurance.repository.ContractRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final ClientRepository clientRepository;

    public ContractService(ContractRepository contractRepository, ClientRepository clientRepository) {
        this.contractRepository = contractRepository;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public ContractResponseDto createContract(ContractCreateDto dto) {
        // 1. Business rule: End date must be after start date
        if (dto.endDate().isBefore(dto.startDate())) {
            throw new RuntimeException("End date cannot be before start date");
        }

        // 2. Check that the client exists
        Client client = clientRepository.findById(dto.clientId())
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + dto.clientId()));

        // 3. Map and prepare the contract
        Contract contract = ContractMapper.toEntity(dto);
        contract.setClient(client);
        contract.setStatus(ContractStatus.ACTIVE);

        // 4. Save
        Contract savedContract = contractRepository.save(contract);

        // 5. Return response
        return ContractMapper.toDto(savedContract);
    }

    @Transactional(readOnly = true)
    public java.util.List<ContractResponseDto> listContracts() {
        return contractRepository.findAll().stream()
                .map(contract -> new ContractResponseDto(
                        contract.getId(),
                        contract.getClient().getId(),
                        contract.getStartDate(),
                        contract.getEndDate(),
                        contract.getStatus(),
                        contract.getCoverageAmount()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public ContractResponseDto getContractById(Long id) {
        Contract contract = contractRepository.findById(id).orElseThrow(() -> new RuntimeException("Contract not found with id: " + id));
        return new ContractResponseDto(
                contract.getId(),
                contract.getClient().getId(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.getStatus(),
                contract.getCoverageAmount()
        );
    }
}

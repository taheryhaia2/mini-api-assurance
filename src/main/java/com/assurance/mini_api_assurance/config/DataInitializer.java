package com.assurance.mini_api_assurance.config;

import com.assurance.mini_api_assurance.domain.*;
import com.assurance.mini_api_assurance.repository.ClientRepository;
import com.assurance.mini_api_assurance.repository.ContractRepository;
import com.assurance.mini_api_assurance.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ContractRepository contractRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           ClientRepository clientRepository,
                           ContractRepository contractRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.contractRepository = contractRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        // 1. Create ADMIN account if it does not exist
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println(">> [DataInit] ADMIN account created (admin/admin123)");
        }

        // 2. Create AGENT account if it does not exist (optional but useful)
        if (userRepository.findByUsername("agent").isEmpty()) {
            User agent = new User();
            agent.setUsername("agent");
            agent.setPassword(passwordEncoder.encode("agent123"));
            agent.setRole(Role.AGENT);
            userRepository.save(agent);
            System.out.println(">> [DataInit] AGENT account created (agent/agent123)");
        }

        // 3. Create demo client + contracts if database is empty
        if (clientRepository.count() == 0) {

            // 3.1 Client
            Client client = new Client();
            client.setLastName("Ben Ali");
            client.setFirstName("Ahmed");
            client.setEmail("ahmed@email.com");
            client.setCin("12345678");
            client.setPhoneNumber("20000000");
            client.setAddress("Tunis, Tunisia");
            client.setBirthDate(LocalDate.of(1990, 5, 15));
            client.setCreatedAt(LocalDate.now());
            clientRepository.save(client);
            System.out.println(">> [DataInit] Client 'Ahmed Ben Ali' created");

            // 3.2 ACTIVE contract
            Contract activeContract = new Contract();
            activeContract.setClient(client);
            activeContract.setPolicyNumber("CT-2026-00001");
            activeContract.setType(ContractType.AUTO);
            activeContract.setStartDate(LocalDate.now().minusMonths(1));
            activeContract.setEndDate(LocalDate.now().plusYears(1));
            activeContract.setCoverageAmount(new BigDecimal("50000"));
            activeContract.setPremiumAmount(new BigDecimal("1200"));
            activeContract.setStatus(ContractStatus.ACTIVE);
            contractRepository.save(activeContract);
            System.out.println(">> [DataInit] ACTIVE contract created for Ahmed");

            // 3.3 TERMINATED contract
            Contract terminatedContract = new Contract();
            terminatedContract.setClient(client);
            terminatedContract.setPolicyNumber("CT-2026-00002");
            terminatedContract.setType(ContractType.HOME);
            terminatedContract.setStartDate(LocalDate.now().minusYears(2));
            terminatedContract.setEndDate(LocalDate.now().minusYears(1));
            terminatedContract.setCoverageAmount(new BigDecimal("30000"));
            terminatedContract.setPremiumAmount(new BigDecimal("800"));
            terminatedContract.setStatus(ContractStatus.TERMINATED);
            contractRepository.save(terminatedContract);
            System.out.println(">> [DataInit] TERMINATED contract created for Ahmed");
        }
    }
}
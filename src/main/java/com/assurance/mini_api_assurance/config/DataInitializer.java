package com.assurance.mini_api_assurance.config;

import com.assurance.mini_api_assurance.domain.*;
import com.assurance.mini_api_assurance.repository.ClientRepository;
import com.assurance.mini_api_assurance.repository.ContratRepository;
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
    private final ContratRepository contratRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           ClientRepository clientRepository,
                           ContratRepository contratRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.contratRepository = contratRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        // 1. Création de l'ADMIN s'il n'existe pas
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println(">> [DataInit] Compte ADMIN créé (admin/admin123)");
        }

        // 2. Création d'un CLIENT de test s'il n'y a aucun client en base
        if (clientRepository.count() == 0) {
            Client client = new Client();
            client.setNom("Ben Ali");
            client.setPrenom("Ahmed");
            client.setEmail("ahmed@email.com");
            client.setCin("12345678");
            client.setDateNaissance(LocalDate.of(1990, 5, 15));
            client.setDateCreation(LocalDate.now());
            clientRepository.save(client);
            System.out.println(">> [DataInit] Client 'Ahmed Ben Ali' créé");

            // 3. Création d'un Contrat ACTIF
            Contrat contratActif = new Contrat();
            contratActif.setClient(client);
            contratActif.setDateDebut(LocalDate.now().minusMonths(1));
            contratActif.setDateFin(LocalDate.now().plusYears(1));
            contratActif.setMontantCouverture(new BigDecimal("50000.0"));
            contratActif.setStatut(StatutContrat.ACTIF);
            contratRepository.save(contratActif);
            System.out.println(">> [DataInit] Contrat ACTIF créé pour Ahmed");

            // 4. Création d'un Contrat RESILIE
            Contrat contratResilie = new Contrat();
            contratResilie.setClient(client);
            contratResilie.setDateDebut(LocalDate.now().minusYears(2));
            contratResilie.setDateFin(LocalDate.now().minusYears(1));
            contratResilie.setMontantCouverture(new BigDecimal("30000.0"));
            contratResilie.setStatut(StatutContrat.RESILIE);
            contratRepository.save(contratResilie);
            System.out.println(">> [DataInit] Contrat RESILIE créé pour Ahmed");
        }
    }
}
package com.assurance.mini_api_assurance.controller;

import com.assurance.mini_api_assurance.dto.ClientCreateDto;
import com.assurance.mini_api_assurance.dto.ClientResponseDto;
import com.assurance.mini_api_assurance.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientResponseDto> creerClient(@Valid @RequestBody ClientCreateDto dto) {
        ClientResponseDto response = clientService.creerClient(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public List<ClientResponseDto> listerClients() {
        return clientService.listerClients();
    }
    @GetMapping("/{id}")
    public ClientResponseDto getClientParId(@PathVariable Long id) {
        return clientService.getClientParId(id);
    }
}
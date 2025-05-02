package com.example.controller;

import com.example.model.DTO.ClientDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface IClientController {
    
    @GetMapping
    ResponseEntity<List<ClientDTO>> getAllClients();
    
    @GetMapping("/{id}")
    ResponseEntity<?> getClientById(@PathVariable Long id);
    
    @GetMapping("/dni/{dni}")
    ResponseEntity<?> getClientByDni(@PathVariable String dni);
    
    @PostMapping
    ResponseEntity<?> createClient(@RequestBody ClientDTO client);
    
    @PutMapping("/{id}")
    ResponseEntity<?> updateClient(@PathVariable Long id, @RequestBody ClientDTO client);
    
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteClient(@PathVariable Long id);
}
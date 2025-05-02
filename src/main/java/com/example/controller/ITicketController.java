package com.example.controller;

import com.example.model.DTO.TicketDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface ITicketController {
    
    @GetMapping
    ResponseEntity<List<TicketDTO>> getAllTickets();
    
    @GetMapping("/{id}")
    ResponseEntity<?> getTicketById(@PathVariable Long id);
    
    @GetMapping("/client/{clientId}")
    ResponseEntity<List<TicketDTO>> getTicketsByClientId(@PathVariable Long clientId);
    
    @PostMapping
    ResponseEntity<?> createTicket(@RequestBody TicketDTO ticket);
    
    @PostMapping("/client/{clientId}")
    ResponseEntity<?> assignAdditionalTicket(@PathVariable Long clientId, @RequestBody TicketDTO ticket);
    
    @PutMapping("/{id}")
    ResponseEntity<?> updateTicket(@PathVariable Long id, @RequestBody TicketDTO ticket);
    
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteTicket(@PathVariable Long id);
}
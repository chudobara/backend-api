package com.example.controller.imp;

import com.example.controller.ITicketController;
import com.example.exceptions.BaseDatosException;
import com.example.exceptions.ClienteFallidoException;
import com.example.exceptions.TicketException;
import com.example.model.Client;
import com.example.model.DTO.ClientDTO;
import com.example.model.DTO.TicketDTO;
import com.example.model.Ticket;
import com.example.service.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
public class TicketControllerImpl implements ITicketController {

    @Autowired
    private ITicketService ticketService;

    @Override
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        try {
            List<Ticket> tickets = ticketService.getAllTickets();
            List<TicketDTO> ticketDTOs = tickets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(ticketDTOs);
        } catch (BaseDatosException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }
    
    @Override
    public ResponseEntity<List<TicketDTO>> getTicketsByClientId(@PathVariable Long clientId) {
        try {
            List<Ticket> tickets = ticketService.getTicketsByClientId(clientId);
            List<TicketDTO> ticketDTOs = tickets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(ticketDTOs);
        } catch (ClienteFallidoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
        } catch (BaseDatosException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    @Override
    public ResponseEntity<?> getTicketById(@PathVariable Long id) {
        try {
            Optional<Ticket> ticketOpt = ticketService.getTicketById(id);
            if (ticketOpt.isPresent()) {
                TicketDTO ticketDTO = convertToDTO(ticketOpt.get());
                return ResponseEntity.ok(ticketDTO);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "No se encontró el ticket con ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (TicketException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (BaseDatosException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error de base de datos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<?> createTicket(@RequestBody TicketDTO ticketDTO) {
        try {
            Ticket ticket = convertToEntity(ticketDTO);
            Ticket savedTicket = ticketService.createTicket(ticket);
            TicketDTO savedTicketDTO = convertToDTO(savedTicket);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTicketDTO);
        } catch (TicketException | ClienteFallidoException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (BaseDatosException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error de base de datos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @Override
    public ResponseEntity<?> assignAdditionalTicket(@PathVariable Long clientId, @RequestBody TicketDTO ticketDTO) {
        try {
            Ticket ticket = convertToEntity(ticketDTO);
            Ticket savedTicket = ticketService.assignAdditionalTicket(clientId, ticket);
            TicketDTO savedTicketDTO = convertToDTO(savedTicket);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTicketDTO);
        } catch (TicketException | ClienteFallidoException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (BaseDatosException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error de base de datos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @Override
    public ResponseEntity<?> updateTicket(@PathVariable Long id, @RequestBody TicketDTO ticketDTO) {
        try {
            Ticket ticket = convertToEntity(ticketDTO);
            Ticket updatedTicket = ticketService.updateTicket(id, ticket);
            TicketDTO updatedTicketDTO = convertToDTO(updatedTicket);
            return ResponseEntity.ok(updatedTicketDTO);
        } catch (TicketException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (BaseDatosException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error de base de datos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<?> deleteTicket(@PathVariable Long id) {
        try {
            ticketService.deleteTicket(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Ticket eliminado con éxito");
            return ResponseEntity.ok(response);
        } catch (TicketException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (BaseDatosException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error de base de datos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // Helper methods to convert between DTO and Entity
    private TicketDTO convertToDTO(Ticket ticket) {
        TicketDTO dto = new TicketDTO();
        dto.setId(ticket.getId());
        
        // Convert Client to ClientDTO
        if (ticket.getCliente() != null) {
            ClientDTO clientDTO = new ClientDTO();
            clientDTO.setId(ticket.getCliente().getId());
            clientDTO.setNombre(ticket.getCliente().getNombre());
            clientDTO.setApellido(ticket.getCliente().getApellido());
            clientDTO.setDni(ticket.getCliente().getDni());
            clientDTO.setFechaNacimiento(ticket.getCliente().getFechaNacimiento());
            dto.setCliente(clientDTO);
        }
        
        dto.setEvento(ticket.getEvento());
        dto.setCosto(ticket.getCosto());
        dto.setFechaVigencia(ticket.getFechaVigencia());
        return dto;
    }
    
    private Ticket convertToEntity(TicketDTO dto) {
        Ticket ticket = new Ticket();
        ticket.setId(dto.getId());
        
        // Convert ClientDTO to Client
        if (dto.getCliente() != null) {
            Client client = new Client();
            client.setId(dto.getCliente().getId());
            client.setNombre(dto.getCliente().getNombre());
            client.setApellido(dto.getCliente().getApellido());
            client.setDni(dto.getCliente().getDni());
            client.setFechaNacimiento(dto.getCliente().getFechaNacimiento());
            ticket.setCliente(client);
        }
        
        ticket.setEvento(dto.getEvento());
        ticket.setCosto(dto.getCosto());
        ticket.setFechaVigencia(dto.getFechaVigencia());
        return ticket;
    }
}
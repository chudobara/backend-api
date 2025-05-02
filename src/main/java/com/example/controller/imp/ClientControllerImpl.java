package com.example.controller.imp;

import com.example.controller.IClientController;
import com.example.exceptions.BaseDatosException;
import com.example.exceptions.ClienteFallidoException;
import com.example.model.Client;
import com.example.model.DTO.ClientDTO;
import com.example.service.IClientService;
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
@RequestMapping("/api/clients")
public class ClientControllerImpl implements IClientController {

    @Autowired
    private IClientService clientService;

    @Override
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        try {
            List<Client> clients = clientService.getAllClients();
            List<ClientDTO> clientDTOs = clients.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(clientDTOs);
        } catch (BaseDatosException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    @Override
    public ResponseEntity<?> getClientById(@PathVariable Long id) {
        try {
            Optional<Client> clientOpt = clientService.getClientById(id);
            if (clientOpt.isPresent()) {
                ClientDTO clientDTO = convertToDTO(clientOpt.get());
                return ResponseEntity.ok(clientDTO);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "No se encontró el cliente con ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (BaseDatosException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error de base de datos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @Override
    public ResponseEntity<?> getClientByDni(@PathVariable String dni) {
        try {
            Client client = clientService.getClientByDni(dni);
            if (client != null) {
                ClientDTO clientDTO = convertToDTO(client);
                return ResponseEntity.ok(clientDTO);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "No se encontró el cliente con DNI: " + dni);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (BaseDatosException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error de base de datos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<?> createClient(@RequestBody ClientDTO clientDTO) {
        try {
            Client client = convertToEntity(clientDTO);
            Client savedClient = clientService.createClient(client);
            ClientDTO savedClientDTO = convertToDTO(savedClient);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedClientDTO);
        } catch (ClienteFallidoException e) {
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Override
    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        try {
            Client client = convertToEntity(clientDTO);
            Client updatedClient = clientService.updateClient(id, client);
            ClientDTO updatedClientDTO = convertToDTO(updatedClient);
            return ResponseEntity.ok(updatedClientDTO);
        } catch (ClienteFallidoException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
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
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        try {
            clientService.deleteClient(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Cliente eliminado con éxito");
            return ResponseEntity.ok(response);
        } catch (ClienteFallidoException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
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
    private ClientDTO convertToDTO(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setNombre(client.getNombre());
        dto.setApellido(client.getApellido());
        dto.setDni(client.getDni());
        dto.setFechaNacimiento(client.getFechaNacimiento());
        return dto;
    }
    
    private Client convertToEntity(ClientDTO dto) {
        Client client = new Client();
        client.setId(dto.getId());
        client.setNombre(dto.getNombre());
        client.setApellido(dto.getApellido());
        client.setDni(dto.getDni());
        client.setFechaNacimiento(dto.getFechaNacimiento());
        return client;
    }
}
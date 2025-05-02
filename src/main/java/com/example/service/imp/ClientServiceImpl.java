package com.example.service.imp;

import com.example.exceptions.BaseDatosException;
import com.example.exceptions.ClienteFallidoException;
import com.example.model.Client;
import com.example.model.entities.ClientEntity;
import com.example.repository.IClientRepository;
import com.example.service.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements IClientService {

    @Autowired
    private IClientRepository clientRepository;

    private static final String VALIDATION_URL = "https://test.paseshow.com.ar/permissions/paseshow/util/technical-test";
    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Override
    public List<Client> getAllClients() throws BaseDatosException {
        try {
            List<ClientEntity> clientEntities = clientRepository.findAll();
            return clientEntities.stream()
                    .map(this::convertToModel)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BaseDatosException("Error al recuperar todos los clientes", e);
        }
    }

    @Override
    public Optional<Client> getClientById(Long id) throws BaseDatosException {
        try {
            Optional<ClientEntity> clientEntity = clientRepository.findById(id);
            return clientEntity.map(this::convertToModel);
        } catch (Exception e) {
            throw new BaseDatosException("Error al buscar el cliente con ID: " + id, e);
        }
    }
    
    @Override
    public Client getClientByDni(String dni) throws BaseDatosException {
        try {
            ClientEntity clientEntity = clientRepository.findByDni(dni);
            return clientEntity != null ? convertToModel(clientEntity) : null;
        } catch (Exception e) {
            throw new BaseDatosException("Error al buscar el cliente con DNI: " + dni, e);
        }
    }

    @Override
    public Client createClient(Client client) throws ClienteFallidoException, BaseDatosException {
        // Validaciones de negocio
        if (client == null) {
            throw new ClienteFallidoException("El cliente no puede ser nulo");
        }
        
        if (client.getDni() == null || client.getDni().trim().isEmpty()) {
            throw new ClienteFallidoException("El DNI del cliente es obligatorio");
        }
        
        if (client.getNombre() == null || client.getNombre().trim().isEmpty()) {
            throw new ClienteFallidoException("El nombre del cliente es obligatorio");
        }
        
        if (client.getApellido() == null || client.getApellido().trim().isEmpty()) {
            throw new ClienteFallidoException("El apellido del cliente es obligatorio");
        }
        
        if (client.getFechaNacimiento() == null) {
            throw new ClienteFallidoException("La fecha de nacimiento del cliente es obligatoria");
        }

        // Validar que el DNI no esté duplicado
        if (clientRepository.existsByDni(client.getDni())) {
            throw new ClienteFallidoException("Ya existe un cliente con el DNI " + client.getDni());
        }

        // Validar el cliente externamente
        validateClientExternally(client.getNombre(), client.getApellido());

        try {
            ClientEntity clientEntity = convertToEntity(client);
            ClientEntity savedEntity = clientRepository.save(clientEntity);
            return convertToModel(savedEntity);
        } catch (Exception e) {
            throw new BaseDatosException("Error al guardar el cliente en la base de datos", e);
        }
    }

    @Override
    public Client updateClient(Long id, Client updatedClient) throws ClienteFallidoException, BaseDatosException {
        // Validaciones de negocio
        if (updatedClient == null) {
            throw new ClienteFallidoException("El cliente actualizado no puede ser nulo");
        }
        
        if (id == null) {
            throw new ClienteFallidoException("El ID del cliente es obligatorio");
        }
        
        // Buscar el cliente existente
        Optional<ClientEntity> existingClientOpt = clientRepository.findById(id);
        if (existingClientOpt.isEmpty()) {
            throw new ClienteFallidoException("No se encontró el cliente con ID: " + id);
        }

        ClientEntity clientEntity = existingClientOpt.get();
        
        // Si se está cambiando el DNI, verificar que no exista otro cliente con ese DNI
        if (!clientEntity.getDni().equals(updatedClient.getDni()) && 
            clientRepository.existsByDni(updatedClient.getDni())) {
            throw new ClienteFallidoException("Ya existe un cliente con el DNI " + updatedClient.getDni());
        }
        
        try {
            clientEntity.setNombre(updatedClient.getNombre());
            clientEntity.setApellido(updatedClient.getApellido());
            clientEntity.setDni(updatedClient.getDni());
            clientEntity.setFechaNacimiento(updatedClient.getFechaNacimiento());

            ClientEntity updatedEntity = clientRepository.save(clientEntity);
            return convertToModel(updatedEntity);
        } catch (Exception e) {
            throw new BaseDatosException("Error al actualizar el cliente en la base de datos", e);
        }
    }

    @Override
    public void deleteClient(Long id) throws ClienteFallidoException, BaseDatosException {
        if (id == null) {
            throw new ClienteFallidoException("El ID del cliente es obligatorio");
        }
        
        if (!clientRepository.existsById(id)) {
            throw new ClienteFallidoException("No se encontró el cliente con ID: " + id);
        }
        
        try {
            clientRepository.deleteById(id);
        } catch (Exception e) {
            throw new BaseDatosException("Error al eliminar el cliente de la base de datos", e);
        }
    }

    private void validateClientExternally(String nombre, String apellido) throws ClienteFallidoException {
        try {
            logger.info("Iniciando validación externa para: {} {}", nombre, apellido);
            
            RestTemplate restTemplate = new RestTemplate();
            
            // Preparar el header con la autorización
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Codificar en Base64 la concatenación del nombre y apellido
            String authValue = Base64.getEncoder().encodeToString((nombre + ":" + apellido).getBytes());
            headers.set("Authorization", authValue);
            logger.debug("Authorization header: {}", authValue);
            
            // Preparar el body de la petición
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("nombre", nombre);
            requestBody.put("apellido", apellido);
            
            // Crear la entidad HTTP con headers y body
            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
            
            // Realizar la petición POST y obtener la respuesta completa
            logger.debug("Enviando solicitud a: {}", VALIDATION_URL);
            
            org.springframework.http.ResponseEntity<String> response = restTemplate.exchange(
                VALIDATION_URL,
                org.springframework.http.HttpMethod.POST,
                requestEntity,
                String.class
            );
            
            // Verificar el status code HTTP
            logger.info("Respuesta recibida con status code: {}", response.getStatusCode());
            String responseBody = response.getBody();
            logger.info("Cuerpo de la respuesta: {}", responseBody);
            
            // Verificar dos condiciones: 
            // 1. El status code debe ser 200 OK
            // 2. El cuerpo de la respuesta debe contener "OK"
            if (!response.getStatusCode().equals(org.springframework.http.HttpStatus.OK)) {
                
                logger.warn("Validación fallida. Status code: {}, Respuesta: {}", 
                    response.getStatusCode(), responseBody);
                throw new ClienteFallidoException("La validación externa del cliente falló");
            }
            
            logger.info("Validación externa exitosa");
        } catch (RestClientException e) {
            logger.error("Error en la validación externa: ", e);
            throw new ClienteFallidoException("Error en la validación externa del cliente: " + e.getMessage(), e);
        } catch (ClienteFallidoException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error inesperado en la validación: ", e);
            throw new ClienteFallidoException("Error inesperado en la validación del cliente: " + e.getMessage(), e);
        }
    }
    
    // Helper methods to convert between model and entity
    private ClientEntity convertToEntity(Client client) {
        ClientEntity entity = new ClientEntity();
        entity.setId(client.getId());
        entity.setNombre(client.getNombre());
        entity.setApellido(client.getApellido());
        entity.setDni(client.getDni());
        entity.setFechaNacimiento(client.getFechaNacimiento());
        return entity;
    }
    
    private Client convertToModel(ClientEntity entity) {
        Client client = new Client();
        client.setId(entity.getId());
        client.setNombre(entity.getNombre());
        client.setApellido(entity.getApellido());
        client.setDni(entity.getDni());
        client.setFechaNacimiento(entity.getFechaNacimiento());
        return client;
    }
}
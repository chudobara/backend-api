package com.example.service.imp;

import com.example.exceptions.BaseDatosException;
import com.example.exceptions.ClienteFallidoException;
import com.example.exceptions.TicketException;
import com.example.model.Client;
import com.example.model.Ticket;
import com.example.model.entities.ClientEntity;
import com.example.model.entities.TicketEntity;
import com.example.repository.IClientRepository;
import com.example.repository.ITicketRepository;
import com.example.service.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements ITicketService {

    @Autowired
    private ITicketRepository ticketRepository;

    @Autowired
    private IClientRepository clientRepository;

    @Override
    public List<Ticket> getAllTickets() throws BaseDatosException {
        try {
            List<TicketEntity> ticketEntities = ticketRepository.findAll();
            return ticketEntities.stream()
                    .map(this::convertToModel)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BaseDatosException("Error al recuperar todos los tickets", e);
        }
    }
    
    @Override
    public List<Ticket> getTicketsByClientId(Long clientId) throws BaseDatosException, ClienteFallidoException {
        try {
            if (clientId == null) {
                throw new ClienteFallidoException("El ID del cliente no puede ser nulo");
            }
            
            Optional<ClientEntity> clientEntity = clientRepository.findById(clientId);
            if (clientEntity.isEmpty()) {
                throw new ClienteFallidoException("No se encontró el cliente con ID: " + clientId);
            }
            
            List<TicketEntity> ticketEntities = ticketRepository.findByCliente(clientEntity.get());
            return ticketEntities.stream()
                    .map(this::convertToModel)
                    .collect(Collectors.toList());
        } catch (ClienteFallidoException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseDatosException("Error al recuperar los tickets del cliente con ID: " + clientId, e);
        }
    }

    @Override
    public Optional<Ticket> getTicketById(Long id) throws BaseDatosException, TicketException {
        try {
            if (id == null) {
                throw new TicketException("El ID del ticket no puede ser nulo");
            }
            
            Optional<TicketEntity> ticketEntity = ticketRepository.findById(id);
            return ticketEntity.map(this::convertToModel);
        } catch (TicketException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseDatosException("Error al buscar el ticket con ID: " + id, e);
        }
    }

    @Override
    public Ticket createTicket(Ticket ticket) throws TicketException, ClienteFallidoException, BaseDatosException {
        // Validaciones de negocio
        if (ticket == null) {
            throw new TicketException("El ticket no puede ser nulo");
        }
        
        Client client = ticket.getCliente();
        if (client == null || client.getId() == null) {
            throw new TicketException("El ticket debe estar asociado a un cliente válido");
        }
        
        if (ticket.getEvento() == null || ticket.getEvento().trim().isEmpty()) {
            throw new TicketException("El evento del ticket es obligatorio");
        }
        
        if (ticket.getCosto() == null || ticket.getCosto() <= 0) {
            throw new TicketException("El costo del ticket debe ser mayor que cero");
        }
        
        if (ticket.getFechaVigencia() == null) {
            throw new TicketException("La fecha de vigencia del ticket es obligatoria");
        }
        
        try {
            // Buscar el cliente en la base de datos
            Optional<ClientEntity> existingClient = clientRepository.findById(client.getId());
            if (existingClient.isEmpty()) {
                throw new ClienteFallidoException("No se encontró el cliente con ID: " + client.getId());
            }
            
            // Convertir ticket a entidad
            TicketEntity ticketEntity = convertToEntity(ticket);
            ticketEntity.setCliente(existingClient.get());
            
            // Guardar el ticket
            TicketEntity savedTicket = ticketRepository.save(ticketEntity);
            return convertToModel(savedTicket);
        } catch (ClienteFallidoException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseDatosException("Error al guardar el ticket en la base de datos", e);
        }
    }
    
    @Override
    public Ticket assignAdditionalTicket(Long clientId, Ticket ticket) throws TicketException, ClienteFallidoException, BaseDatosException {
        // Validaciones de negocio
        if (clientId == null) {
            throw new ClienteFallidoException("El ID del cliente no puede ser nulo");
        }
        
        if (ticket == null) {
            throw new TicketException("El ticket no puede ser nulo");
        }
        
        if (ticket.getEvento() == null || ticket.getEvento().trim().isEmpty()) {
            throw new TicketException("El evento del ticket es obligatorio");
        }
        
        if (ticket.getCosto() == null || ticket.getCosto() <= 0) {
            throw new TicketException("El costo del ticket debe ser mayor que cero");
        }
        
        if (ticket.getFechaVigencia() == null) {
            throw new TicketException("La fecha de vigencia del ticket es obligatoria");
        }
        
        try {
            // Buscar el cliente
            Optional<ClientEntity> existingClient = clientRepository.findById(clientId);
            if (existingClient.isEmpty()) {
                throw new ClienteFallidoException("No se encontró el cliente con ID: " + clientId);
            }
            
            // Verificar si el cliente ya tiene un ticket existente
            boolean clientHasExistingTicket = ticketRepository.existsByCliente(existingClient.get());
            if (!clientHasExistingTicket) {
                throw new ClienteFallidoException("El cliente debe tener al menos un ticket existente para recibir tickets adicionales");
            }
            
            // Convertir y asignar el ticket al cliente
            TicketEntity ticketEntity = convertToEntity(ticket);
            ticketEntity.setCliente(existingClient.get());
            
            // Guardar el ticket
            TicketEntity savedTicket = ticketRepository.save(ticketEntity);
            return convertToModel(savedTicket);
        } catch (ClienteFallidoException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseDatosException("Error al asignar el ticket al cliente en la base de datos", e);
        }
    }

    @Override
    public Ticket updateTicket(Long id, Ticket updatedTicket) throws TicketException, BaseDatosException {
        // Validaciones de negocio
        if (id == null) {
            throw new TicketException("El ID del ticket no puede ser nulo");
        }
        
        if (updatedTicket == null) {
            throw new TicketException("El ticket actualizado no puede ser nulo");
        }
        
        if (updatedTicket.getEvento() == null || updatedTicket.getEvento().trim().isEmpty()) {
            throw new TicketException("El evento del ticket es obligatorio");
        }
        
        if (updatedTicket.getCosto() == null || updatedTicket.getCosto() <= 0) {
            throw new TicketException("El costo del ticket debe ser mayor que cero");
        }
        
        if (updatedTicket.getFechaVigencia() == null) {
            throw new TicketException("La fecha de vigencia del ticket es obligatoria");
        }
        
        try {
            Optional<TicketEntity> existingTicketOpt = ticketRepository.findById(id);
            if (existingTicketOpt.isEmpty()) {
                throw new TicketException("No se encontró el ticket con ID: " + id);
            }
            
            TicketEntity ticketEntity = existingTicketOpt.get();
            
            // Solo actualizamos los campos que no sean el cliente
            ticketEntity.setEvento(updatedTicket.getEvento());
            ticketEntity.setCosto(updatedTicket.getCosto());
            ticketEntity.setFechaVigencia(updatedTicket.getFechaVigencia());
            
            TicketEntity updatedEntity = ticketRepository.save(ticketEntity);
            return convertToModel(updatedEntity);
        } catch (TicketException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseDatosException("Error al actualizar el ticket en la base de datos", e);
        }
    }

    @Override
    public void deleteTicket(Long id) throws TicketException, BaseDatosException {
        if (id == null) {
            throw new TicketException("El ID del ticket no puede ser nulo");
        }
        
        try {
            if (!ticketRepository.existsById(id)) {
                throw new TicketException("No se encontró el ticket con ID: " + id);
            }
            
            ticketRepository.deleteById(id);
        } catch (TicketException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseDatosException("Error al eliminar el ticket de la base de datos", e);
        }
    }
    
    // Helper methods for entity-model conversion
    private TicketEntity convertToEntity(Ticket ticket) {
        TicketEntity entity = new TicketEntity();
        entity.setId(ticket.getId());
        entity.setEvento(ticket.getEvento());
        entity.setCosto(ticket.getCosto());
        entity.setFechaVigencia(ticket.getFechaVigencia());
        return entity;
    }
    
    private Ticket convertToModel(TicketEntity entity) {
        Ticket ticket = new Ticket();
        ticket.setId(entity.getId());
        
        // Convertir ClientEntity a Client
        if (entity.getCliente() != null) {
            Client client = new Client();
            client.setId(entity.getCliente().getId());
            client.setNombre(entity.getCliente().getNombre());
            client.setApellido(entity.getCliente().getApellido());
            client.setDni(entity.getCliente().getDni());
            client.setFechaNacimiento(entity.getCliente().getFechaNacimiento());
            ticket.setCliente(client);
        }
        
        ticket.setEvento(entity.getEvento());
        ticket.setCosto(entity.getCosto());
        ticket.setFechaVigencia(entity.getFechaVigencia());
        return ticket;
    }
}
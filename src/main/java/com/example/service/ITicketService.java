package com.example.service;

import com.example.exceptions.BaseDatosException;
import com.example.exceptions.ClienteFallidoException;
import com.example.exceptions.TicketException;
import com.example.model.Ticket;

import java.util.List;
import java.util.Optional;

public interface ITicketService {
    
    List<Ticket> getAllTickets() throws BaseDatosException;
    
    List<Ticket> getTicketsByClientId(Long clientId) throws BaseDatosException, ClienteFallidoException;
    
    Optional<Ticket> getTicketById(Long id) throws BaseDatosException, TicketException;
    
    Ticket createTicket(Ticket ticket) throws TicketException, ClienteFallidoException, BaseDatosException;
    
    Ticket assignAdditionalTicket(Long clientId, Ticket ticket) throws TicketException, ClienteFallidoException, BaseDatosException;
    
    Ticket updateTicket(Long id, Ticket updatedTicket) throws TicketException, BaseDatosException;
    
    void deleteTicket(Long id) throws TicketException, BaseDatosException;
}
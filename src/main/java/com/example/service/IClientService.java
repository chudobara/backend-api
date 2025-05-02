package com.example.service;

import com.example.exceptions.BaseDatosException;
import com.example.exceptions.ClienteFallidoException;
import com.example.model.Client;

import java.util.List;
import java.util.Optional;

public interface IClientService {
    
    List<Client> getAllClients() throws BaseDatosException;
    
    Optional<Client> getClientById(Long id) throws BaseDatosException;
    
    Client getClientByDni(String dni) throws BaseDatosException;
    
    Client createClient(Client client) throws ClienteFallidoException, BaseDatosException;
    
    Client updateClient(Long id, Client updatedClient) throws ClienteFallidoException, BaseDatosException;
    
    void deleteClient(Long id) throws ClienteFallidoException, BaseDatosException;
}
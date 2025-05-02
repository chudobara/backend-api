package com.example.repository;

import com.example.model.entities.ClientEntity;
import com.example.model.entities.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITicketRepository extends JpaRepository<TicketEntity, Long> {
    List<TicketEntity> findByCliente(ClientEntity cliente);
    boolean existsByCliente(ClientEntity cliente);
    long countByCliente(ClientEntity cliente);
}
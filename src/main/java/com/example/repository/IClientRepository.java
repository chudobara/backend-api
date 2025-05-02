package com.example.repository;

import com.example.model.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IClientRepository extends JpaRepository<ClientEntity, Long> {
    boolean existsByDni(String dni);
    ClientEntity findByDni(String dni);
}
package com.example.repository;

import com.example.model.entities.ClientEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
public class ClientRepositoryTest {
    
    @Autowired
    private IClientRepository clientRepository;
    
    @Test
    public void whenSaved_thenFindsByDni() {
        // Given
        ClientEntity client = new ClientEntity();
        client.setNombre("John");
        client.setApellido("Doe");
        client.setDni("12345678A");
        client.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        
        // When
        clientRepository.save(client);
        
        // Then
        assertThat(clientRepository.existsByDni("12345678A")).isTrue();
    }
    
    @Test
    public void whenNotSaved_thenNotFindsByDni() {
        // When/Then
        assertThat(clientRepository.existsByDni("99999999Z")).isFalse();
    }
}
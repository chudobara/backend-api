package com.example.model.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ticket")
public class TicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private ClientEntity cliente;

    @Column(nullable = false)
    private String evento;

    @Column(nullable = false)
    private Double costo;

    @Column(nullable = false)
    private LocalDate fechaVigencia;

    // Constructores
    public TicketEntity() {
    }

    public TicketEntity(ClientEntity cliente, String evento, Double costo, LocalDate fechaVigencia) {
        this.cliente = cliente;
        this.evento = evento;
        this.costo = costo;
        this.fechaVigencia = fechaVigencia;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClientEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClientEntity cliente) {
        this.cliente = cliente;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public LocalDate getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(LocalDate fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }
}
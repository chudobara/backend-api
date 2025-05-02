package com.example.model.DTO;

import java.time.LocalDate;

public class TicketDTO {
    private Long id;
    private ClientDTO cliente;
    private String evento;
    private Double costo;
    private LocalDate fechaVigencia;

    // Constructores
    public TicketDTO() {
    }

    public TicketDTO(Long id, ClientDTO cliente, String evento, Double costo, LocalDate fechaVigencia) {
        this.id = id;
        this.cliente = cliente;
        this.evento = evento;
        this.costo = costo;
        this.fechaVigencia = fechaVigencia;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClientDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClientDTO cliente) {
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
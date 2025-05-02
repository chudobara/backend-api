package com.example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {
    
    @Autowired
    private Environment environment;
    
    @Value("${server.port:8080}")
    private String serverPort;
    
    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "running");
        response.put("message", "Backend API está ejecutándose correctamente");
        
        // Agregar información sobre los perfiles activos
        response.put("profiles", Arrays.asList(environment.getActiveProfiles()));
        
        // Agregar endpoints disponibles
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("clientes", "/api/clients");
        endpoints.put("tickets", "/api/tickets");
        if (Arrays.asList(environment.getActiveProfiles()).contains("dev")) {
            endpoints.put("h2-console", "/h2-console");
        }
        response.put("endpoints", endpoints);
        
        return response;
    }
    
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        return status;
    }
}
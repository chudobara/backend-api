package com.example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/db-info")
public class DatabaseInfoController {

    @Value("${spring.datasource.url}")
    private String dbUrl;
    
    @Value("${spring.datasource.username}")
    private String dbUsername;
    
    @Value("${spring.datasource.driver-class-name:${spring.datasource.driverClassName:unknown}}")
    private String dbDriver;
    
    @Value("${spring.h2.console.enabled:false}")
    private boolean h2ConsoleEnabled;
    
    @Value("${spring.h2.console.path:/h2-console}")
    private String h2ConsolePath;

    @GetMapping
    public Map<String, Object> getDatabaseInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("url", dbUrl);
        info.put("username", dbUsername);
        info.put("driver", dbDriver);
        info.put("h2ConsoleEnabled", h2ConsoleEnabled);
        info.put("h2ConsolePath", h2ConsolePath);
        
        // Add connection instructions
        if (h2ConsoleEnabled) {
            Map<String, String> connectionInstructions = new HashMap<>();
            connectionInstructions.put("console_url", "http://localhost:8080" + h2ConsolePath);
            connectionInstructions.put("jdbc_url", dbUrl);
            connectionInstructions.put("username", dbUsername);
            connectionInstructions.put("password", "");
            info.put("connection_instructions", connectionInstructions);
        }
        
        return info;
    }
}
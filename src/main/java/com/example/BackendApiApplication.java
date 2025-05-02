package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

@EnableJpaRepositories(basePackages = "com.example.repository")
@EntityScan(basePackages = {"com.example.model", "com.example.model.entities"})
@SpringBootApplication
public class BackendApiApplication implements CommandLineRunner {

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(BackendApiApplication.class, args);
    }
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Perfil activo: " + String.join(", ", environment.getActiveProfiles()));
        
        boolean isDevProfile = false;
        for (String profile : environment.getActiveProfiles()) {
            if (profile.equals("dev")) {
                isDevProfile = true;
                break;
            }
        }
        
        if (isDevProfile) {
            System.out.println("Ejecutando en modo desarrollo con H2 Database");
            System.out.println("Accede a la consola H2: http://localhost:8080/h2-console");
            System.out.println("JDBC URL: jdbc:h2:mem:devdb");
            System.out.println("Usuario: sa");
            System.out.println("Contraseña: (vacía)");
        }
    }
}
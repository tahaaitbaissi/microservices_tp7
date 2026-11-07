package org.example;

import org.example.entity.Compte;
import org.example.entity.TypeCompte;
import org.example.repository.CompteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Date;

@SpringBootApplication
public class MsBanqueApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsBanqueApplication.class, args);
    }

    @Bean
    CommandLineRunner initialiser(CompteRepository registre) {
        return args -> {
            registre.save(new Compte(null, 4500.0, Date.valueOf("2024-02-14"), TypeCompte.EPARGNE));
            registre.save(new Compte(null, 7200.5, Date.valueOf("2024-06-03"), TypeCompte.COURANT));
            registre.save(new Compte(null, 1200.0, Date.valueOf("2025-01-20"), TypeCompte.EPARGNE));

            registre.findAll().forEach(compte -> System.out.println(compte.toString()));
        };
    }
}

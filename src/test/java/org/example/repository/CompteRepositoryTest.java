package org.example.repository;

import org.example.entity.Compte;
import org.example.entity.TypeCompte;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CompteRepositoryTest {

    @Autowired
    private CompteRepository registre;

    @Test
    @DisplayName("save + findAll persiste et relit les comptes")
    void persisteEtRelit() {
        long avant = registre.count();
        registre.save(new Compte(null, 4500.0, Date.valueOf("2024-02-14"), TypeCompte.EPARGNE));
        registre.save(new Compte(null, 7200.5, Date.valueOf("2024-06-03"), TypeCompte.COURANT));

        List<Compte> comptes = registre.findAll();

        assertEquals(avant + 2, comptes.size());
        assertTrue(comptes.stream().anyMatch(c -> c.getMontant().equals(7200.5)));
    }

    @Test
    @DisplayName("findById + deleteById retrouve puis supprime")
    void retrouvePuisSupprime() {
        long avant = registre.count();
        Compte sauve = registre.save(new Compte(null, 1200.0, Date.valueOf("2025-01-20"), TypeCompte.EPARGNE));

        assertTrue(registre.findById(sauve.getId()).isPresent());

        registre.deleteById(sauve.getId());

        assertEquals(avant, registre.count());
    }
}

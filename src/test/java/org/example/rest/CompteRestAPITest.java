package org.example.rest;

import org.example.entity.Compte;
import org.example.entity.TypeCompte;
import org.example.repository.CompteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompteRestAPITest {

    @Mock
    private CompteRepository registre;

    @InjectMocks
    private CompteRestAPI resource;

    private Compte compte(Long id, Double montant, String jour, TypeCompte type) {
        return new Compte(id, montant, Date.valueOf(jour), type);
    }

    @Test
    @DisplayName("GET /banque/comptes renvoie tous les comptes")
    void listerRenvoieLesComptes() {
        List<Compte> attendus = Arrays.asList(
                compte(1L, 4500.0, "2024-02-14", TypeCompte.EPARGNE),
                compte(2L, 7200.5, "2024-06-03", TypeCompte.COURANT));
        when(registre.findAll()).thenReturn(attendus);

        List<Compte> resultat = resource.lister();

        assertEquals(2, resultat.size());
        assertEquals(7200.5, resultat.get(1).getMontant());
    }

    @Test
    @DisplayName("GET /banque/comptes/{id} renvoie le compte demande")
    void detailsRenvoieLeCompte() {
        when(registre.findById(1L)).thenReturn(Optional.of(compte(1L, 4500.0, "2024-02-14", TypeCompte.EPARGNE)));

        Compte resultat = resource.details(1L);

        assertEquals(TypeCompte.EPARGNE, resultat.getType());
    }

    @Test
    @DisplayName("GET /banque/comptes/{id} renvoie null si absent")
    void detailsRenvoieNullSiAbsent() {
        when(registre.findById(404L)).thenReturn(Optional.empty());

        assertNull(resource.details(404L));
    }

    @Test
    @DisplayName("POST /banque/comptes persiste et renvoie le compte")
    void ouvrirPersisteLeCompte() {
        Compte nouveau = compte(null, 980.0, "2025-09-11", TypeCompte.COURANT);
        when(registre.save(any(Compte.class))).thenReturn(compte(4L, 980.0, "2025-09-11", TypeCompte.COURANT));

        Compte resultat = resource.ouvrir(nouveau);

        assertEquals(4L, resultat.getId());
        verify(registre).save(nouveau);
    }

    @Test
    @DisplayName("PUT /banque/comptes/{id} met a jour montant date et type")
    void modifierMetAJourLeCompte() {
        Compte existant = compte(2L, 7200.5, "2024-06-03", TypeCompte.COURANT);
        when(registre.findById(2L)).thenReturn(Optional.of(existant));
        when(registre.save(any(Compte.class))).thenAnswer(inv -> inv.getArgument(0));

        Compte resultat = resource.modifier(2L, compte(null, 1500.0, "2025-09-11", TypeCompte.EPARGNE));

        assertEquals(1500.0, resultat.getMontant());
        assertEquals(TypeCompte.EPARGNE, resultat.getType());
        assertEquals(Date.valueOf("2025-09-11"), resultat.getDateOuverture());
    }

    @Test
    @DisplayName("PUT /banque/comptes/{id} renvoie null si absent")
    void modifierRenvoieNullSiAbsent() {
        when(registre.findById(404L)).thenReturn(Optional.empty());

        assertNull(resource.modifier(404L, compte(null, 1.0, "2025-01-01", TypeCompte.COURANT)));
    }

    @Test
    @DisplayName("DELETE /banque/comptes/{id} supprime par identifiant")
    void fermerSupprimeLeCompte() {
        resource.fermer(3L);

        verify(registre).deleteById(3L);
    }
}

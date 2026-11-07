package org.example.rest;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.example.entity.Compte;
import org.example.repository.CompteRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Path("/banque")
public class CompteRestAPI {

    private final CompteRepository registre;

    public CompteRestAPI(CompteRepository registre) {
        this.registre = registre;
    }

    @Path("/comptes")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Compte> lister() {
        return registre.findAll();
    }

    @Path("/comptes/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Compte details(@PathParam("id") Long id) {
        return registre.findById(id).orElse(null);
    }

    @Path("/comptes")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Compte ouvrir(Compte compte) {
        return registre.save(compte);
    }

    @Path("/comptes/{id}")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Compte modifier(@PathParam("id") Long id, Compte compte) {
        Compte existant = registre.findById(id).orElse(null);
        if (existant != null) {
            existant.setMontant(compte.getMontant());
            existant.setDateOuverture(compte.getDateOuverture());
            existant.setType(compte.getType());
            return registre.save(existant);
        }
        return null;
    }

    @Path("/comptes/{id}")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void fermer(@PathParam("id") Long id) {
        registre.deleteById(id);
    }
}

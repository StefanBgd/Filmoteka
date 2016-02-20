/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.filmoteka.service;

import java.util.List;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import rs.filmoteka.domain.Film;
import rs.filmoteka.emf.EMF;
import rs.filmoteka.emf.Manager;

/**
 *
 * @author stefan
 */
@Path("films")
public class FilmEndpoint {
    
     private Manager manager = new Manager();

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCities(@HeaderParam("authorization") String authorization,@DefaultValue("10") @QueryParam("limit") int limit, @DefaultValue("1")@QueryParam("page") int page) {
        EntityManager em = EMF.createEntityManager();
        manager.checkUser(em, authorization);
        List<Film> films = em.createNamedQuery("Film.findAll", Film.class).setFirstResult((page-1) * limit).setMaxResults(limit).getResultList();
        em.close();
        GenericEntity<List<Film>> entity = new GenericEntity<List<Film>>(films) {};
        return Response.ok().entity(entity).build();
    }
    
    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getFilm(@HeaderParam("authorization") String authorization, @PathParam("id") String id) {
        EntityManager em = EMF.createEntityManager();
        manager.checkUser(em, authorization);
        Film film = em.createNamedQuery("Film.findByFilmID", Film.class).setParameter("filmID", id).getSingleResult();
        em.close();
        GenericEntity<Film> entity = new GenericEntity<Film>(film) {};
        return Response.ok().entity(entity).build();
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addFilm(@HeaderParam("authorization") String authorization, Film film) {
        EntityManager em = EMF.createEntityManager();
        manager.checkUser(em, authorization);
        manager.persist(em, film);
        em.close();
        return Response.ok(film).build();
    }
    
    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response editFilm(@HeaderParam("authorization") String authorization, Film film) {
        EntityManager em = EMF.createEntityManager();
        manager.merge(em, film);
        em.close();
        return Response.ok(film).build();
    }
    
    @DELETE
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteFilm(@HeaderParam("authorization") String authorization, @PathParam("id") String id) {
        EntityManager em = EMF.createEntityManager();
        manager.checkUser(em, authorization);
        Film film = em.createNamedQuery("Film.findByFilmID", Film.class).setParameter("filmID", id).getSingleResult();
        manager.remove(em, film);
        em.close();
        return Response.ok(film).build();
    }
    
}

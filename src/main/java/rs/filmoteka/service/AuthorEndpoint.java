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
import rs.filmoteka.domain.Author;
import rs.filmoteka.emf.EMF;
import rs.filmoteka.emf.Manager;

/**
 *
 * @author stefan
 */
@Path("authors")
public class AuthorEndpoint {

    private Manager manager = new Manager();

    /**
     * getAuthors vraća sve registrovane autore.
     * URL: http://localhost:8084/Filmoteka/rest/authors 
     * METHOD: GET 
     * HEADERS: Authorization
     * @param authorization [String]
     * @param limit [int]
     * @param page [int]
     * @return [application/json, application/xml] Lista autora
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAuthors(@HeaderParam("authorization") String authorization, @DefaultValue("10") @QueryParam("limit") int limit, @DefaultValue("1") @QueryParam("page") int page) {
        EntityManager em = EMF.createEntityManager();
        manager.checkUser(em, authorization);
        List<Author> authors = em.createNamedQuery("Author.findAll", Author.class).setFirstResult((page - 1) * limit).setMaxResults(limit).getResultList();
        em.close();
        GenericEntity<List<Author>> entity = new GenericEntity<List<Author>>(authors) {
        };
        return Response.ok().entity(entity).build();
    }

    /**
     * getAuthor vraća autora za definisani id.
     * URL: http://localhost:8084/Filmoteka/rest/authors/:id 
     * METHOD: GET
     * HEADERS: Authorization
     * @param authorization [String]
     * @param id [int]
     * @return [application/json, application/xml] Autor
     */
    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAuthor(@HeaderParam("authorization") String authorization, @PathParam("id") int id) {
        EntityManager em = EMF.createEntityManager();
        manager.checkUser(em, authorization);
        Author author = em.createNamedQuery("Author.findByAuthorID", Author.class).setParameter("authorID", id).getSingleResult();
        em.close();
        GenericEntity<Author> entity = new GenericEntity<Author>(author) {
        };
        return Response.ok().entity(entity).build();
    }

    /**
     * addAuthor dodaje novog autora.
     * URL: http://localhost:8084/Filmoteka/rest/authors 
     * METHOD: POST
     * HEADERS: Authorization, Content-Type
     * @param authorization [String]
     * @param author [Author object]
     * @return [application/json, application/xml] Autor
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addAuthor(@HeaderParam("authorization") String authorization, Author author) {
        EntityManager em = EMF.createEntityManager();
        manager.checkUser(em, authorization);
        manager.persist(em, author);
        em.close();
        return Response.ok(author).build();
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response editAuthor(@HeaderParam("authorization") String authorization, Author author) {
        EntityManager em = EMF.createEntityManager();
        manager.merge(em, author);
        em.close();
        return Response.ok(author).build();
    }

    
     /**
     * deleteAuthor briše autora za id.
     * URL: http://localhost:8084/Filmoteka/rest/authors/:id 
     * METHOD: DELETE
     * HEADERS: Authorization
     * @param authorization [String]
     * @param id [int]
     * @return [application/json, application/xml] obrisan Autor
     */
    @DELETE
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteAuthor(@HeaderParam("authorization") String authorization, @PathParam("id") int id) {
        EntityManager em = EMF.createEntityManager();
        manager.checkUser(em, authorization);
        Author author = em.createNamedQuery("Author.findByAuthorID", Author.class).setParameter("authorID", id).getSingleResult();
        manager.remove(em, author);
        em.close();
        return Response.ok(author).build();
    }

}

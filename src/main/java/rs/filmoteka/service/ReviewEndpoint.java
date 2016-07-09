/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.filmoteka.service;

import java.util.Date;
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
import rs.filmoteka.domain.Review;
import rs.filmoteka.domain.User;
import rs.filmoteka.emf.EMF;
import rs.filmoteka.emf.Manager;
import rs.filmoteka.token.AbstractTokenCreator;
import rs.filmoteka.token.Base64Token;

/**
 *
 * @author stefan
 */
@Path("reviews")
public class ReviewEndpoint {

    private Manager manager = new Manager();
    AbstractTokenCreator tokenHelper;
    
    
        public ReviewEndpoint() {
        tokenHelper = new Base64Token();
    }
    
     /**
     * getReviews vraća sve recenzije.
     * URL: http://localhost:8084/Filmoteka/rest/reviews 
     * METHOD: GET 
     * HEADERS: Authorization
     * @param authorization [String]
     * @param limit [int]
     * @param page [int]
     * @return [application/json, application/xml] Lista recenzija
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getReviews(@HeaderParam("authorization") String authorization, @DefaultValue("3") @QueryParam("limit") int limit, @DefaultValue("1") @QueryParam("page") int page) {
        EntityManager em = EMF.createEntityManager();
        manager.checkUser(em, authorization);
        List<Review> reviews = em.createNamedQuery("Review.findAll", Review.class).setFirstResult((page - 1) * limit).setMaxResults(limit).getResultList();
        em.close();
        GenericEntity<List<Review>> entity = new GenericEntity<List<Review>>(reviews) {
        };
        return Response.ok().entity(entity).build();
    }

     /**
     * getReview vraća recenziju za definisani id.
     * URL: http://localhost:8084/Filmoteka/rest/reviews/:id 
     * METHOD: GET
     * HEADERS: Authorization
     * @param authorization [String]
     * @param id [int]
     * @return [application/json, application/xml] Review
     */
    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getReview(@HeaderParam("authorization") String authorization, @PathParam("id") int id) {
        EntityManager em = EMF.createEntityManager();
        manager.checkUser(em, authorization);
        Review review = em.createNamedQuery("Review.findByReviewID", Review.class).setParameter("reviewID", id).getSingleResult();
        em.close();
        GenericEntity<Review> entity = new GenericEntity<Review>(review) {
        };
        return Response.ok().entity(entity).build();
    }

        /**
     * addReview dodaje novu recenziju.
     * URL: http://localhost:8084/Filmoteka/rest/reviews 
     * METHOD: POST
     * HEADERS: Authorization, Content-Type
     * @param authorization [String]
     * @param review [Review object]
     * @return [application/json, application/xml] Review
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addReview(@HeaderParam("Authorization") String authorization, Review review) {
        EntityManager em = EMF.createEntityManager();
        manager.checkAuthor(em, authorization);

        Integer id = Integer.parseInt(tokenHelper.decode(authorization).split("##")[1]);
        User user = em.createNamedQuery("User.findByUserID", User.class).setParameter("userID", id).getSingleResult();
        review.setDatum(new Date());
        review.setAuthorID(user.getAuthorID());
        
        manager.persist(em, review);
        em.close();
        return Response.ok(review).build();
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response editReview(@HeaderParam("authorization") String authorization, Review review) {
        EntityManager em = EMF.createEntityManager();
        manager.merge(em, review);
        em.close();
        return Response.ok(review).build();
    }

         /**
     * deleteReview briše recenziju za id.
     * URL: http://localhost:8084/Filmoteka/rest/reviews/:id 
     * METHOD: DELETE
     * HEADERS: Authorization
     * @param authorization [String]
     * @param id [int]
     * @return [application/json, application/xml] obrisan Review
     */
    @DELETE
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteReview(@HeaderParam("authorization") String authorization, @PathParam("id") int id) {
        EntityManager em = EMF.createEntityManager();
        manager.checkUser(em, authorization);
        Review review = em.createNamedQuery("Review.findByReviewID", Review.class).setParameter("reviewID", id).getSingleResult();
        manager.remove(em, review);
        em.close();
        return Response.ok(review).build();
    }

}

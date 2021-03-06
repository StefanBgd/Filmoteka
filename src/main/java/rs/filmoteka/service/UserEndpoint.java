/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.filmoteka.service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import rs.filmoteka.domain.Author;
import rs.filmoteka.domain.User;
import rs.filmoteka.domain.UserType;
import rs.filmoteka.emf.EMF;
import rs.filmoteka.emf.Manager;
import rs.filmoteka.exception.NotAuthorizedException;
import rs.filmoteka.pojo.UserRepresentation;
import rs.filmoteka.token.AbstractTokenCreator;
import rs.filmoteka.token.Base64Token;

/**
 *
 * @author stefan
 */
@Path("users")
public class UserEndpoint {

    AbstractTokenCreator tokenHelper;
    Manager manager = new Manager();

    public UserEndpoint() {
        tokenHelper = new Base64Token();
    }

    /**
     * checkToken proverava token korisnika.
     * URL: http://localhost:8084/Filmoteka/rest/users 
     * METHOD: GET 
     * HEADERS:Authorization
     * @param token [String]
     * @return status 200 OK
     */
    @GET
    public Response checkToken(@HeaderParam("authorization") String token) {
        try {
            EntityManager em = EMF.createEntityManager();
            Integer id = Integer.parseInt(tokenHelper.decode(token).split("##")[1]);
            User user = em.createNamedQuery("User.findByUserID", User.class).setParameter("userID", id).getSingleResult();
            return Response.ok().build();
        } catch (Exception e) {
            throw new NotAuthorizedException(e.getMessage());
        }
    }

    /**
     * logIn uloguje korisnika na sajt, odnosno vrati mu token.
     * URL: http://localhost:8084/Filmoteka/rest/users/login
     * METHOD: POST 
     * HEADERS: Authorization
     * @param authorization [String]
     * @return [application/json] UserRepresentation
     */
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logIn(@HeaderParam("authorization") String authorization) {

        String[] userPass;
        EntityManager em = EMF.createEntityManager();
        try {
            userPass = tokenHelper.decodeBasicAuth(authorization);
        } catch (RuntimeException e) {
            throw new NotAuthorizedException(e.getMessage());
        }
        User user = null;
        try {
            user = (User) em
                    .createQuery("SELECT u FROM User u WHERE u.username = :userName")
                    .setParameter("userName", userPass[0])
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new NotAuthorizedException("This username doesn't exist");
        }

        if (!userPass[1].equals(user.getPass())) {
            throw new NotAuthorizedException("Wrong password");
        }

        if (user.getToken() == null || user.getToken().equals("")) {
            user.setToken(tokenHelper.createToken(user.getUserID()));
            manager.merge(em, user);
        }
        UserRepresentation ur = new UserRepresentation(user.getUsername(), tokenHelper.encode(user.getToken()));
        return Response.ok().entity(ur).build();
    }

     /**
     * logout izloguje korisnika sa sajta.
     * URL: http://localhost:8084/Filmoteka/rest/users/logout
     * METHOD: POST 
     * HEADERS: Authorization
     * @param token [String]
     * @return status 200 OK
     */
    @POST
    @Path("/logout")
    public Response logOut(@HeaderParam("authorization") String token) {
        EntityManager em = EMF.createEntityManager();
        manager.checkUser(em, token); //!!
        User user = em.find(User.class, Integer.parseInt(tokenHelper.decode(token).split("##")[1]));
        user.setToken(null);
        manager.merge(em, user);
        return Response.ok().build();
    }

     /**
     * register registruje korisnika.
     * URL: http://localhost:8084/Filmoteka/rest/users/register
     * METHOD: GET 
     * HEADERS: Authorization
     * @param imePrezime
     * @param grad
     * @param username
     * @param password
     * @param type
     * @return status 200 OK
     */
    @GET
    @Path("/register")
    public Response register(@QueryParam("imePrezime") String imePrezime, @QueryParam("grad") String grad, @QueryParam("username") String username, @QueryParam("password") String password, @DefaultValue("2") @QueryParam("type") Integer type) {
        try {
            EntityManager em = EMF.createEntityManager();
            User user = new User();
            if (type == 2) {
                Author author = new Author();
                author.setGrad(grad);
                author.setImePrezime(imePrezime);
                manager.persist(em, author);
                user.setAuthorID(author);
            }

            user.setUsername(username);
            user.setPass(password);
            user.setTypeID(type);
            manager.persist(em, user);
            return Response.ok().build();
        } catch (Exception e) {
            throw new NotAuthorizedException(e.getMessage());
        }
    }
}

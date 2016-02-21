/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.filmoteka.emf;

import javax.persistence.EntityManager;
import rs.filmoteka.domain.User;
import rs.filmoteka.exception.NotAuthorizedException;
import rs.filmoteka.token.AbstractTokenCreator;
import rs.filmoteka.token.Base64Token;

/**
 *
 * @author stefan
 */
public class Manager {

    AbstractTokenCreator tokenHelper;

    public AbstractTokenCreator getAbstractToken() {
        return tokenHelper;
    }

    public Manager() {
        tokenHelper = new Base64Token();
    }

    public void persist(EntityManager em, Object o) {
        em.getTransaction().begin();
        em.persist(o);
        em.getTransaction().commit();
    }

    public void remove(EntityManager em, Object o) {
        em.getTransaction().begin();
        em.remove(o);
        em.getTransaction().commit();
    }

    public void merge(EntityManager em, Object o) {
        em.getTransaction().begin();
        em.merge(o);
        em.getTransaction().commit();
    }

    public void checkUser(EntityManager em, String token) {
        try {
            User user = em.find(User.class, Integer.parseInt(tokenHelper.decode(token).split("##")[1]));
            if (user.getToken() != null && !user.getToken().equals("")) {
//                if (!havePrivilege(em, user, tableId, method)) {
//                    throw new ForbbidenException("You don't have permission to search data");
//                }
            } else {
                em.close();
                throw new NotAuthorizedException("You are not logged in!");
            }
        } catch (NullPointerException | ArrayIndexOutOfBoundsException | IllegalArgumentException ex) {
            em.close();
            throw new NotAuthorizedException(ex.getMessage());
        }
    }

    public void checkAuthor(EntityManager em, String token) {
        try {
            User user = em.find(User.class, Integer.parseInt(tokenHelper.decode(token).split("##")[1]));
            if (user.getToken() != null && !user.getToken().equals("")) {
                if (!(user.getAuthorID() != null && user.getAuthorID().getAuthorID() == 2)) {
                    em.close();
                    throw new NotAuthorizedException("You have no premission for this request");
                }
            } else {
                em.close();
                throw new NotAuthorizedException("You are not logged in!");
            }
        } catch (NullPointerException | ArrayIndexOutOfBoundsException | IllegalArgumentException ex) {
            em.close();
            throw new NotAuthorizedException(ex.getMessage());
        }
    }
}

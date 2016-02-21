/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.filmoteka.service;

import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import rs.filmoteka.emf.EMF;
import rs.filmoteka.emf.Manager;

/**
 *
 * @author stefan
 */
@Path("statistics")
public class Statistics {

    private Manager manager = new Manager();

    @GET
    @Path("marks")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getStat(@HeaderParam("authorization") String authorization) {
        EntityManager em = EMF.createEntityManager();
        manager.checkUser(em, authorization);
        List<Object[]> stats = em.createNativeQuery("SELECT ocena, count(*) FROM filmoteka1.review group by ocena").getResultList();
        em.close();
        HashMap hm = new HashMap();
        for (Object[] stat : stats) {
            hm.put(stat[0], stat[1]);
        }
        GenericEntity<HashMap> entity = new GenericEntity<HashMap>(hm) {
        };
        return Response.ok().entity(entity).build();
    }

    @GET
    @Path("reviewers")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getRewStats(@HeaderParam("authorization") String authorization) {
        EntityManager em = EMF.createEntityManager();
        manager.checkUser(em, authorization);
        List<Object[]> stats = em.createNativeQuery("SELECT a.imePrezime, count(*) FROM filmoteka1.review r left join filmoteka1.author a on (r.authorID=a.authorID) group by r.authorID;").getResultList();
        em.close();
        HashMap hm = new HashMap();
        for (Object[] stat : stats) {
            hm.put(stat[0], stat[1]);
        }
        GenericEntity<HashMap> entity = new GenericEntity<HashMap>(hm) {
        };
        return Response.ok().entity(entity).build();
    }

}

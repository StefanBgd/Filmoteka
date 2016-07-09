/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.filmoteka.service;

import java.util.ArrayList;
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

        
     /**
     * getMarkAndCount vraća ocene i njihovu količinu.
     * URL: http://localhost:8084/Filmoteka/rest/statistics/marks 
     * METHOD: GET 
     * HEADERS: Authorization
     * @param authorization [String]
     * @return [application/json, application/xml] Lista ocena i njihove količine
     */
    @GET
    @Path("marks")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getStat(@HeaderParam("authorization") String authorization) {
        EntityManager em = EMF.createEntityManager();
        manager.checkUser(em, authorization);
        List<Object[]> stats = em.createNativeQuery("SELECT ocena, count(*) FROM filmoteka1.review group by ocena").getResultList();
        em.close();
        List<HashMap> li = new ArrayList();
        for (Object[] stat : stats) {
            HashMap hm = new HashMap();
            hm.put("ocena", stat[0]);
            hm.put("broj", stat[1]);
            li.add(hm);
        }
        GenericEntity<List<HashMap>> entity = new GenericEntity<List<HashMap>>(li) {
        };
        return Response.ok().entity(entity).build();
    }
     /**
     * getMarkAndCount vraća ime autora i broj njegovih recenzija.
     * URL: http://localhost:8084/Filmoteka/rest/statistics/reviewers 
     * METHOD: GET 
     * HEADERS: Authorization
     * @param authorization [String]
     * @return [application/json, application/xml] Lista autora i broja recenzija
     */
    @GET
    @Path("reviewers")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getRewStats(@HeaderParam("authorization") String authorization) {
        EntityManager em = EMF.createEntityManager();
        manager.checkUser(em, authorization);
        List<Object[]> stats = em.createNativeQuery("SELECT a.imePrezime, count(*) FROM filmoteka1.review r left join filmoteka1.author a on (r.authorID=a.authorID) group by r.authorID;").getResultList();
        em.close();
        List<HashMap> li = new ArrayList();
        for (Object[] stat : stats) {
            HashMap hm = new HashMap();
            hm.put("ime", stat[0]);
            hm.put("broj", stat[1]);
            li.add(hm);
        }
        GenericEntity<List<HashMap>> entity = new GenericEntity<List<HashMap>>(li) {
        };
        return Response.ok().entity(entity).build();
    }

}

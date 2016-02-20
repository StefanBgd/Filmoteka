/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.filmoteka.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author stefan
 */
public abstract class WorldException extends WebApplicationException {

    public WorldException(String message, int code, Response.Status status) {
        super(Response.status(status).entity(new ErrorMessage(message, code)).type(MediaType.APPLICATION_JSON).build());
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.filmoteka.exception;

import javax.ws.rs.core.Response;

/**
 *
 * @author stefan
 */
public class NotAuthorizedException extends WorldException {

    public NotAuthorizedException(String message) {
        super(message, 401, Response.Status.UNAUTHORIZED);
    }
}

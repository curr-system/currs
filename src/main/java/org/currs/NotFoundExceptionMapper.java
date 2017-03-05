package org.currs;

import org.currs.resoruces.Error;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    public Response toResponse(NotFoundException exception) {
        Error e = new Error(Response.Status.NOT_FOUND);
        return e.getResponse();
    }
}

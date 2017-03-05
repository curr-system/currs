package org.currs;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    public Response toResponse(NotFoundException exception) {
        Response.Status error = Response.Status.NOT_FOUND;
        return Response.status(error)
                .entity(String.format("{error:\"%s\",code:%d}",
                        error.getReasonPhrase(), error.getStatusCode()))
                .build();
    }
}

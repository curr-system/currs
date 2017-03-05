package org.currs.resoruces;

import javax.ws.rs.core.Response;

/**
 * Resource represents error code
 */
public class Error {

    private Response.Status status;

    public Error(Response.Status status) {
        this.status = status;
    }

    public Response.Status getStatus() {
        return status;
    }

    public Response getResponse() {
        return Response.status(getStatus())
                .entity(toString())
                .build();
    }

    @Override
    public String toString() {
        return String.format("{error:\"%s\",code:%d}",
                status.getReasonPhrase(), status.getStatusCode());
    }
}

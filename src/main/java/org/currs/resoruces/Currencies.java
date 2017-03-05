package org.currs.resoruces;

import org.currs.model.IRepository;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Currencies REST resource
 */
@Path("currencies")
public class Currencies {

    private IRepository repository;

    @Inject
    public Currencies(IRepository repository) {
        this.repository = repository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        String[] currencies = repository.getAvailableCurrencies();

        for (int i = 0; i < currencies.length; i++) {
            currencies[i] = String.format("{name:\"%s\",links:[{href:\"/currencies/%s\",rel:\"data\"}]}", currencies[i], currencies[i]);
        }

        return Response.status(Response.Status.OK)
                .entity("{currencies:[" + String.join(",", currencies) + "]}")
                .build();
    }
}

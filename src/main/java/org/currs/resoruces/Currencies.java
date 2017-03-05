package org.currs.resoruces;

import org.currs.model.IRepository;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;

/**
 * Currencies REST resource
 */
@Path("currencies")
public class Currencies {

    /**
     * Data source
     */
    private IRepository repository;

    /**
     * No content response
     */
    private static final Response NO_CONTENT_RESPONSE = Response
            .status(Response.Status.NO_CONTENT).entity("[]").build();

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
                .entity("[" + String.join(",", currencies) + "]")
                .build();
    }

    @GET
    @Path("/{currency}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("currency") String currency) {
        if (currency == null || currency.trim() == "") {
            return NO_CONTENT_RESPONSE;
        }

        String[] currencies = repository.getAvailableCurrencies();
        if(!Arrays.asList(currencies).contains(currency)) {
            return NO_CONTENT_RESPONSE;
        }

        // 2016-09-06
        int year = 2016;
        int month = 9;
        int day = 6;
        int days = 7;

        String[] data = repository.getCurrencyData(currency, days, year, month, day);
        return Response.status(Response.Status.OK)
                .entity("[" + String.join(",", data) + "]")
                .build();
    }
}

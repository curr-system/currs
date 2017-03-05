package org.currs.resoruces;

import org.currs.model.IRepository;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * Currencies REST resource
 */
@Path("currencies")
public class Currencies {

    /**
     * No content response
     */
    private static final Response NO_CONTENT_RESPONSE = Response
            .status(Response.Status.NO_CONTENT).entity("[]").build();

    /**
     * Default value for "days" parameter
     */
    private static final int DEFAULT_DAYS_VALUE = 7;

    /**
     * Data source
     */
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
                .entity("[" + String.join(",", currencies) + "]")
                .build();
    }

    @GET
    @Path("/{currency}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("currency") String currency,
                        @QueryParam("days")  Integer days,
                        @QueryParam("year")  Integer year,
                        @QueryParam("month") Integer month,
                        @QueryParam("day")   Integer day) {
        if (currency == null || currency.trim() == "") {
            return NO_CONTENT_RESPONSE;
        }

        String[] currencies = repository.getAvailableCurrencies();
        if(!Arrays.asList(currencies).contains(currency)) {
            return NO_CONTENT_RESPONSE;
        }

        DateTime dt = new DateTime();
        if (day == null) day = dt.getDayOfMonth();
        if (month == null) month = dt.getMonthOfYear();
        if (year == null) year = dt.getYear();

        if (days == null) days = DEFAULT_DAYS_VALUE;

        String[] data = repository.getCurrencyData(currency, days, year, month, day);
        return Response.status(Response.Status.OK)
                .entity("[" + String.join(",", data) + "]")
                .build();
    }
}

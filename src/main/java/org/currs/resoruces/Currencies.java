package org.currs.resoruces;

import org.currs.model.IRepository;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Currencies REST resource
 */
@Path("currencies")
public class Currencies {

    private static final Response NO_CONTENT_RESPONSE = Response
            .status(Response.Status.NO_CONTENT).entity("[]").build();

    private static final Response BAD_REQUEST_RESPONSE = Response
            .status(Response.Status.BAD_REQUEST).entity("[]").build();

    private static final int DAYS_DEFAULT_VALUE = 7;
    private static final int DAYS_MIN_VALUE = 1;
    private static final int DAYS_MAX_VALUE = 90;

    private static final int FREQUENCY_DEFAULT_VALUE = 60;

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
                        @QueryParam("days")      Integer days,
                        @QueryParam("year")      Integer year,
                        @QueryParam("month")     Integer month,
                        @QueryParam("day")       Integer day,
                        @QueryParam("frequency") Integer frequency) {

        // validate given currency name
        if (currency == null || currency.trim() == "") {
            return NO_CONTENT_RESPONSE;
        }

        String[] currencies = repository.getAvailableCurrencies();
        if(!Arrays.asList(currencies).contains(currency)) {
            return NO_CONTENT_RESPONSE;
        }

        // validate days query param
        if (days == null) {
            days = DAYS_DEFAULT_VALUE;
        } else if (days < DAYS_MIN_VALUE || days > DAYS_MAX_VALUE) {
            return BAD_REQUEST_RESPONSE;
        }

        // validate date query params
        DateTime dt = new DateTime();
        if (day == null)   day   = dt.getDayOfMonth();
        if (month == null) month = dt.getMonthOfYear();
        if (year == null)  year  = dt.getYear();

        DateTime qdt = new DateTime(year, month, day, 0, 0);
        if (qdt.isAfterNow()) {
            return BAD_REQUEST_RESPONSE;
        }

        // validate frequency param
        if (frequency == null) {
            frequency = FREQUENCY_DEFAULT_VALUE;
        } else {
            return BAD_REQUEST_RESPONSE;
        }

        // get data from repository and apply frequency
        String[] data = repository.getCurrencyData(currency, days, year, month, day);
        data = applyFrequencyOnData(data, frequency);

        return Response.status(Response.Status.OK)
                .entity("[" + String.join(",", data) + "]")
                .build();
    }

    private String[] applyFrequencyOnData(String[] data, Integer frequency) {
        ArrayList<String> result = new ArrayList<String>();

        if (data == null || data.length == 0) {
            return new String[0];
        }

        if (frequency == 10) {
            return data.clone();
        }

        // "{"date":"2016-09-06 22:13:40","open":"4.1308","close":"3.9614","min":"4.2107","max":"3.5416"}"
        String format = "{\"date\":\"%s\",\"open\":\"%s\",\"close\":\"%s\",\"min\":\"%.4f\",\"max\":\"%.4f\"}";
        String pattern = "\"date\":\"(.+?)\",\"open\":\"(.+?)\",\"close\":\"(.+?)\",\"min\":\"(.+?)\",\"max\":\"(.+?)\"";
        Pattern r = Pattern.compile(pattern);

        String dateTime = null;
        String open = null;
        String close = null;
        float max, tmax;
        float min, tmin;

        int dataPerSample = frequency/10;
        for (int i = 0; i < data.length; i += dataPerSample) {
            Matcher m = r.matcher(data[i]);
            if (m.find()) {
                dateTime = m.group(1);
                close = m.group(3);
                min = Float.parseFloat(m.group(4));
                max = Float.parseFloat(m.group(5));

                int j = 0;
                for (j = 0; j < dataPerSample && i + j < data.length; j++) {
                    Matcher tm = r.matcher(data[i + j]);
                    if (tm.find()) {
                        tmin = Float.parseFloat(tm.group(4));
                        tmax = Float.parseFloat(tm.group(5));

                        if (tmin < min) min = tmin;
                        if (tmax > max) max = tmax;
                    }
                }

                Matcher endm = r.matcher(data[i + j - 1]);
                if (endm.find()) {
                    open = endm.group(2);
                    result.add(String.format(format, dateTime, open, close, min, max));
                }
            }
        }

        return result.toArray(new String[0]);
    }
}

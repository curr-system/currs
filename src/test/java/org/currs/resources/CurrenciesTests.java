package org.currs.resources;

import org.currs.model.MockRepository;
import org.currs.resoruces.Currencies;

import org.json.JSONArray;
import org.json.JSONObject;

import org.junit.Before;
import junit.framework.TestCase;

import javax.ws.rs.core.Response;


/**
 * Unit tests for main Currencies REST resource
 */
public class CurrenciesTests extends TestCase {

    Currencies curr;
    MockRepository repo;

    @Before
    public void setUp() throws Exception {
        // create mock repository
        repo = new MockRepository();

        // and currencies resource with this repository
        curr = new Currencies(repo);
    }

    public void testReturnsListOfCurrencies() {
        // get currencies from mock repository
        String[] currencies = repo.getAvailableCurrencies();

        // ask resource for data
        Response response = curr.get();
        String json = "{currencies:"+(String)response.getEntity()+"}";
        System.out.println(json);

        // check response
        JSONObject jsonObject = new JSONObject(json);
        assertTrue(jsonObject.has("currencies"));

        JSONArray c = jsonObject.optJSONArray("currencies");
        assertNotNull("currencies array not found", c);
        assertEquals(currencies.length, c.length());

        for (int i = 0; i < currencies.length; i++) {
            JSONObject o = c.getJSONObject(i);
            assertTrue("No name at index: " + i, o.has("name"));
            assertEquals(currencies[i], o.get("name"));
        }
    }

    public void testResultHasLinks() {
        // get currencies from mock repository
        String[] currencies = repo.getAvailableCurrencies();

        // ask resource for data
        Response response = curr.get();
        String json = "{currencies:"+(String)response.getEntity()+"}";
        System.out.println(json);

        // check response
        JSONObject jsonObject = new JSONObject(json);
        assertTrue(jsonObject.has("currencies"));

        JSONArray c = jsonObject.optJSONArray("currencies");
        assertNotNull("currencies array not found", c);
        assertEquals(currencies.length, c.length());

        for (int i = 0; i < currencies.length; i++) {
            JSONObject o = c.getJSONObject(i);
            assertTrue(o.has("links"));

            JSONArray a = o.optJSONArray("links");
            assertNotNull(a);

            assertTrue(a.length() > 0);
        }
    }

    public void testReturnsErrorForNonExistingCurrency() {
        // ask resource for data
        Response response = curr.get("BLA", null, null, null, null, null);

        // check response
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    public void testReturnsSelectedCurrencyData() {
        // ask resource for data
        Response response = curr.get("CHF", null, 2016, 9, 6, null);
        String json = "{data:" + response.getEntity() + "}";
        System.out.println(json);

        // check response
        JSONObject jsonObject = new JSONObject(json);
        assertTrue(jsonObject.has("data"));

        JSONArray d = jsonObject.optJSONArray("data");
        assertNotNull("data array not found", d);

        int length = d.length();
        assertEquals(167, length);

        for (int i = 0; i < length; i++) {
            JSONObject o = d.getJSONObject(i);
            assertTrue(o.has("date"));
            assertTrue(o.has("open"));
            assertTrue(o.has("close"));
            assertTrue(o.has("min"));
            assertTrue(o.has("max"));
        }
    }
}

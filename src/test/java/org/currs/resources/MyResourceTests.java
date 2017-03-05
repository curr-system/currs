package org.currs;

import org.junit.After;
import org.junit.Before;
import junit.framework.TestCase;

import org.glassfish.grizzly.http.server.HttpServer;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * Unit tests for MyResource
 */
public class MyResourceTests extends TestCase
{
    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        // start the server
        server = App.startServer();

        // create the client
        Client c = ClientBuilder.newClient();
        target = c.target(App.BASE_URI);
    }

    @After
    public void tearDown() throws Exception {
        // stop the server
        server.shutdown();
    }

    /**
     * Check if server is running and returns "Got it!" response for "myresource"
     */
    public void testGetIt() {
        String responseMsg = target.path("myresource").request().get(String.class);
        assertEquals("Got it!", responseMsg);
    }

}

package org.currs;

import org.currs.App;
import org.junit.After;
import org.junit.Before;
import junit.framework.TestCase;

import org.glassfish.grizzly.http.server.HttpServer;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.Set;

/**
 * Unit tests for main App class
 */
public class AppTests extends TestCase {

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
     * Checks if client is able to connect with server
     */
    public void testConnectsWithServer() {
        Exception e = null;
        try {
            target.path("").request().get(String.class);
        } catch (Exception ex) {
            if (!ex.getClass().getName().contains("NotFoundException"))
                e = ex;
        }

        assertNull(e);
    }
}

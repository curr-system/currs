package org.currs;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 *
 */
public class App {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:7777/currs/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.underdog.jersey.grizzly package
        final ResourceConfig rc = new ResourceConfig().packages("org.currs");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();

        System.out.println(String.format("Server is running at: %s\n" +
                "Press CTRL+C to stop..." , BASE_URI));

        // intercept CTRL+C
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (server != null && server.isStarted()) {
                    server.shutdown();
                }
                System.out.println("Server closed");
            }
         });

        // block until CTRL+C
        while (true) {
            System.in.read();
        }
    }
}

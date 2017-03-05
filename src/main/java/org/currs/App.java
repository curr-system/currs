package org.currs;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * Main class.
 * Starts server and waits for CTRL+C
 */
public class App {

    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:7777/currs/";

    /**
     * Starts Grizzly HTTP server
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        final ResourceConfig rc = new AppConfig().packages("org.currs");

        // create and start a new instance of grizzly http server
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     * Starts server and waits for CTRL+C
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

package ch.zhaw.robopatrol;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Main {

    private static Optional<HttpServer> server = Optional.empty();

    private Main(int port) throws IOException {
        URI baseUri = UriBuilder.fromUri("http://0.0.0.0/").port(port).build();
        ResourceConfig config = new RobopatrolServer();
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);

        Main.server = Optional.of(server);
        System.out.println("robopatrol-server: Running on port " + port + ".");
    }

    public static void main(String[] args) throws IOException {
        new Main(9998);
    }

    public static void stop() {
        server.ifPresent(server ->
            server.shutdown(10, TimeUnit.SECONDS)
        );
    }

}

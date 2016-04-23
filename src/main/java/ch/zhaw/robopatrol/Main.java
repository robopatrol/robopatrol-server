package ch.zhaw.robopatrol;

import com.sun.net.httpserver.HttpServer;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static org.glassfish.jersey.server.model.Parameter.Source.URI;

public class Main {

    private static final int PORT = 9998;

    private static Optional<HttpServer> server = Optional.empty();

    public static void main(String[] args) throws IOException {
        start(PORT);
    }

    public static void start(int port) {
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(port).build();
        ResourceConfig config = new RobopatrolServer();
        server = Optional.of(JdkHttpServerFactory.createHttpServer(baseUri, config));
        System.out.println("robopatrol-server: Running on port " + port + ".");
    }

    public static void stop() {
        server.ifPresent(server -> {
            server.stop(3);
        });
    }

}

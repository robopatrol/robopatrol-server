package ch.zhaw.robopatrol;

import com.sun.net.httpserver.HttpServer;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

public class Main {

    private static final int PORT = 9998;

    public static void main(String[] args) throws IOException {
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(PORT).build();
        ResourceConfig config = new RobopatrolServer();
        HttpServer server = JdkHttpServerFactory.createHttpServer(baseUri, config);
        System.out.println("robopatrol-server: Running on port " + PORT + ".");
//        while (System.in.read() != 'q') {
//            System.out.println("Press \"q\" to stop the server.");
//        }
//        server.stop(0);
    }

}
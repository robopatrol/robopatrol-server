package ch.zhaw.robopatrol;

import ch.zhaw.robopatrol.res.DummyResource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import javax.ws.rs.NotFoundException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test that the server can be started.
 * This ensures that the server is configured and that the resources are being loaded.
 */
public class ServerTest {

    @BeforeClass
    public static void startServer() throws IOException {
        Main.main(new String[0]);
        waitForServer(9998);
    }

    @Test(timeout = 30000)
    public void test200() throws IOException {
        try (Scanner response = new Scanner(url(DummyResource.PATH).openStream(), StandardCharsets.UTF_8.name())) {
            String line = response.useDelimiter("\\n").next();
            assertThat(line, is(DummyResource.CONTENT));
        }
    }

    @Test(timeout = 30000, expected = FileNotFoundException.class)
    public void test404() throws IOException {
        try{
            new Scanner(url("somegarbagepath").openStream(), StandardCharsets.UTF_8.name());
        } finally {
            Main.stop();
        }
    }

    @AfterClass
    public static void stopServer() {
        Main.stop();
    }

    private static void waitForServer(int port) throws IOException {
        boolean serverAvailable = false;
        do {
            Socket socket = new Socket("localhost", port);
            serverAvailable = socket.isConnected();
        } while (!serverAvailable);
    }

    private static URL url(String path) throws MalformedURLException {
        return new URL("http://localhost:9998/" + path);
    }

}

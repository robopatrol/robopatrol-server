package ch.zhaw.robopatrol;

import ch.zhaw.robopatrol.res.DummyResource;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test that the server can be started.
 * This ensures that the server is configured and that the resources are being loaded.
 */
public class ServerTest {

    @Test(timeout = 10000)
    public void test() throws IOException {
        int port = findPort();
        Main.start(port);
        waitForServer(port);

        URL testUrl = new URL("http://localhost:" + port + "/" + DummyResource.PATH);
        try (Scanner response = new Scanner(testUrl.openStream())) {
            String line = response.useDelimiter("\\n").next();
            assertThat(line, is(DummyResource.CONTENT));
        }

    }

    private static int findPort() throws IOException {
        int port;
        try (ServerSocket socket = new ServerSocket(0)) {
            port = socket.getLocalPort();
            socket.close();
            return port;
        }
    }

    private static void waitForServer(int port) throws IOException {
        boolean serverAvailable = false;
        do {
            Socket socket = new Socket("localhost", port);
            serverAvailable = socket.isConnected();
        } while (!serverAvailable);
    }

}
package ch.zhaw.robopatrol;

import ch.zhaw.robopatrol.res.DummyResource;
import org.junit.Test;

import java.io.IOException;
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


    @Test(timeout = 30000)
    public void testMain() throws IOException {
        //int port = findPort();
        Main.main(new String[0]);
        //Main.start(port);
        waitForServer(9998);

        URL testUrl = new URL("http://localhost:" + 9998 + "/" + DummyResource.PATH);
        try (Scanner response = new Scanner(testUrl.openStream(),StandardCharsets.UTF_8.name())) {
            String line = response.useDelimiter("\\n").next();
            assertThat(line, is(DummyResource.CONTENT));
        }
        Main.stop();
    }

    private static void waitForServer(int port) throws IOException {
        boolean serverAvailable = false;
        do {
            Socket socket = new Socket("localhost", port);
            serverAvailable = socket.isConnected();
        } while (!serverAvailable);
    }

}

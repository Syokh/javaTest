import binary.tree.telnet.TelnetServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TelnetServerTest {

    private final SocketAddress socketAddress = new InetSocketAddress("localhost", 5555);
    private Socket socketes;
    private ServerLauncherHelper helper;


    private void startServerInBackground() throws InterruptedException {
        helper = new ServerLauncherHelper(new TelnetServer("5555", "C:\\home\\pictures"));
        new Thread(helper).start();
        Thread.sleep(3000);
    }


    @Before
    public void setUp() throws Exception {
        startServerInBackground();
        socketes = new Socket();

        socketes.connect(socketAddress, 10000);
        assertNotNull(new BufferedReader(new InputStreamReader(socketes.getInputStream())).readLine());
    }

    @Test
    public void testInvokeStatus() throws IOException {

        final BufferedReader in = new BufferedReader(new InputStreamReader(socketes.getInputStream()));
        final PrintWriter writer = new PrintWriter(socketes.getOutputStream(), true);

        writer.println("4 ind");
        writer.println("4 001");
        System.out.println("\n");
        for (int i = 0; i < 8; i++) {
            final String message = in.readLine();
            System.out.println("TelnetServerTest Files = " + i + " i " + message);
        }

        writer.println("r 001");
        final String message2 = in.readLine();
        assertEquals("First element Not Number", message2);
    }

    @After
    public void teardown() throws Exception {

        // close the client.
        socketes.close();

        // shutdown the server
        helper.shutdown();

    }


}

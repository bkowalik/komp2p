package client.logic;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Host extends Communicator {
    private final ServerSocket server;
    private final int PORT;

    public Host(int port) throws IOException {
        PORT = port;
        server = ServerSocketFactory.getDefault().createServerSocket(PORT);
        socket = server.accept();
    }
}

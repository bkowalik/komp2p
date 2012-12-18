package client.logic;

import java.io.*;
import java.net.ServerSocket;

public class Host extends AbstractCommunicator {
    private final ServerSocket server;
    private final int PORT;

    public Host(int port) throws IOException {
        PORT = port;
        server = new ServerSocket(port);
        initialize();
    }

    protected void initialize() {
        try { socket = server.accept();}
        catch(IOException e) { e.printStackTrace(); }
        super.initialize();
    }
}

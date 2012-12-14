package client.logic;

import java.io.*;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;

public class Host extends Communicator {
    private final ServerSocket server;
    private final int PORT;

    public Host(int port) throws IOException {
        PORT = port;
        server = new ServerSocket(port);
        initialize();
    }

    protected void initialize() {
        try { socket = server.accept(); }
        catch(IOException e) { e.printStackTrace(); }
        try {
            outWorker = new OutWorker(socket.getOutputStream(), outMessages);
            inWorker = new InWorker(socket.getInputStream(), inMessages);
        } catch(IOException e) { e.printStackTrace(); }
    }

    @Override
    public void run() {
        execute(outWorker);
        execute(inWorker);
    }
}

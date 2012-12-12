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
    }

    public void start() {
        try {
            socket = server.accept();
        } catch(IOException e) {
            e.printStackTrace();
        }

        ExecutorService exec = Executors.newFixedThreadPool(2);
        InWorker inWorker = null;
        OutWorker outWorker = null;
        try {
            inWorker = new InWorker(socket.getInputStream(), incommingMessages);
            outWorker = new OutWorker(socket.getOutputStream(), outcommingMessages);
        } catch(IOException e) {
            e.printStackTrace();
        }

        while(true) {
            exec.execute(inWorker);
            exec.execute(outWorker);
        }
    }
}

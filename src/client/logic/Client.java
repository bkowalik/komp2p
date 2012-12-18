package client.logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class Client extends Communicator {
    private InetSocketAddress address;

    public Client(String address, int port) throws IOException {
        this.address = new InetSocketAddress(address, port);
        socket = new Socket(address, port);
//        socket.shutdownOutput();
        initialize();
    }


    protected void initialize() {
        try {
            inWorker = new InWorker(socket.getInputStream(), inMessages);
            outWorker = new OutWorker(socket.getOutputStream(), outMessages);
        } catch(IOException e) { e.printStackTrace(); }
    }

    @Override
    public void run() {
        execute(inWorker);
        execute(outWorker);
    }
}

package client.logic;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client extends AbstractCommunicator {
    private InetSocketAddress address;

    public Client(String address, int port) throws IOException {
        this.address = new InetSocketAddress(address, port);
        socket = new Socket(address, port);
        initialize();
    }
}

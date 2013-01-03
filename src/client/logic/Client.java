package client.logic;

import java.io.IOException;
import java.net.Socket;

class Client extends Com {
    public Client(String address, int port, int timeout, String id) throws IOException {
        this.id = id;
        socket = new Socket(address, port);
    }
}
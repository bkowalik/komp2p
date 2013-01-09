package client.logic;

import client.event.ConnectionEvent;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

class Client extends Com implements Runnable {
    private final String address;
    private final int port;
    private final int timeout;

    public Client(String address, int port, int timeout, String id) throws IOException {
        this.id = id;
//        socket = new Socket(address, port);

        socket = new Socket();
        this.address = address;
        this.port = port;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        try {
            socket.connect(new InetSocketAddress(address, port), timeout);
            initialize();
        } catch (UnknownHostException e) {
            fireConnectionEvent(new ConnectionEvent(this, e.getMessage(), ConnectionEvent.Type.UnknownHost));
            return;
        } catch (IOException e) {
//            e.printStackTrace();
            fireConnectionEvent(new ConnectionEvent(this, e.getMessage(), ConnectionEvent.Type.TimeoutException));
            return;
        }
        start();
    }
}
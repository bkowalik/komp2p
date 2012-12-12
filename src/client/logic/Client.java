package client.logic;

import javax.net.SocketFactory;
import java.io.*;

public class Client extends Communicator {
    private DataInputStream input;
    private DataOutputStream output;

    public Client(String address, int port) throws IOException {
        socket = SocketFactory.getDefault().createSocket(address, port);
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
    }


    public void start() {

    }
}

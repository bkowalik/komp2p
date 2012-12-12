package client.logic;

import agh.po.Message;

import javax.net.SocketFactory;
import java.io.*;
import java.net.Socket;

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

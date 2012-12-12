package client.logic;

import agh.po.Message;

import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class Communicator {
    protected Message inMessage;
    protected Message outMessage;
    protected Socket socket;
    protected ConcurrentLinkedQueue<Message> incommingMessages;
    protected ConcurrentLinkedQueue<Message> outcommingMessages;

    public abstract void start();
}

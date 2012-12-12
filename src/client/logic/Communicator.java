package client.logic;

import agh.po.Message;

import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class Communicator {
    protected Socket socket;
    final ConcurrentLinkedQueue<Message> inMessages = new ConcurrentLinkedQueue<Message>();
    final ConcurrentLinkedQueue<Message> outMessages = new ConcurrentLinkedQueue<Message>();

    public abstract void start();

    /**
     * Zwraca i <b>usuwa</b> wiadomość z kolejki
     * @return wiadomośc
     */
    public Message readMessage() {
        return inMessages.poll();
    }

    /**
     * Dopisuje wiadomość do kolejki
     * @param msg wiadomość
     */
    public void writeMessage(Message msg) {
        outMessages.add(msg);
    }
}

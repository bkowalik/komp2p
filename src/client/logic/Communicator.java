package client.logic;

import agh.po.Message;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Communicator {
    protected Socket socket;
    final ConcurrentLinkedQueue<Message> inMessages = new ConcurrentLinkedQueue<Message>();
    final ConcurrentLinkedQueue<Message> outMessages = new ConcurrentLinkedQueue<Message>();

    public void start() {
        ExecutorService exec = Executors.newFixedThreadPool(2);
        InWorker inWorker = null;
        OutWorker outWorker = null;
        try {
            inWorker = new InWorker(socket.getInputStream(), inMessages);
            outWorker = new OutWorker(socket.getOutputStream(), outMessages);
        } catch(IOException e) {
            e.printStackTrace();
        }

        exec.execute(inWorker);
        exec.execute(outWorker);
    }

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

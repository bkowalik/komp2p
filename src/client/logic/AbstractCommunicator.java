package client.logic;

import agh.po.Message;
import client.logic.events.NetEventListener;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

public abstract class AbstractCommunicator extends Thread {
    protected Socket socket;
    final BlockingQueue<Message> newInMessages = new LinkedBlockingQueue<Message>();
    final BlockingQueue<Message> newOutMessages = new LinkedBlockingQueue<Message>();
    final ConcurrentLinkedQueue<Message> inMessages = new ConcurrentLinkedQueue<Message>();
    final ConcurrentLinkedQueue<Message> outMessages = new ConcurrentLinkedQueue<Message>();
    private LinkedList<NetEventListener> listeners = new LinkedList<NetEventListener>();
    protected InWorker inWorker;
    protected OutWorker outWorker;
    private ExecutorService exec = Executors.newFixedThreadPool(2);

    protected void initialize() {
        try {
            outWorker = new OutWorker(socket.getOutputStream(), outMessages);
            inWorker = new InWorker(socket.getInputStream(), inMessages);
        } catch(IOException e) { e.printStackTrace(); }
    }
    public void run() {
        exec.execute(outWorker);
        exec.execute(inWorker);
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

    public Queue<Message> getAllIncommingMsgs() {
        return inMessages;
    }

    public BlockingQueue<Message> getIncommingMsgs() {
        return newInMessages;
    }

    public synchronized void addOnMessageListener(NetEventListener lst) {
        listeners.add(lst);
    }

    public synchronized void fireOnMessageReceived() {
        for(NetEventListener nel : listeners) {
            nel.onMessageIncome();
        }
    }
}

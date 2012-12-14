package client.logic;

import agh.po.Message;
import client.logic.events.NetEventListener;

import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Communicator extends Thread {
    protected Socket socket;
    final ConcurrentLinkedQueue<Message> inMessages = new ConcurrentLinkedQueue<Message>();
    final ConcurrentLinkedQueue<Message> outMessages = new ConcurrentLinkedQueue<Message>();
    private LinkedList<NetEventListener> listeners = new LinkedList<NetEventListener>();
    protected InWorker inWorker;
    protected OutWorker outWorker;
    private ExecutorService exec = Executors.newFixedThreadPool(2);

    protected abstract void initialize();
    public abstract void run();

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

    public synchronized void addOnMessageListener(NetEventListener lst) {
        listeners.add(lst);
    }

    public synchronized void fireOnMessageReceived() {
        for(NetEventListener nel : listeners) {
            nel.onMessageIncome();
        }
    }

    protected void execute(Runnable r) {
        exec.execute(r);
    }
}

package client.logic;

import agh.po.Message;
import client.gui.events.NetEventListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

/**
 *
 */
public abstract class Com {
    protected static final int DEFAULT_TIMEOUT = 60000;
    protected Socket socket;
    protected InWorker inWorker;
    protected OutWorker outWorker;
    private ExecutorService exec = Executors.newFixedThreadPool(2);
    private final ConcurrentLinkedQueue<Message> inMessages = new ConcurrentLinkedQueue<Message>();
    private final ConcurrentLinkedQueue<Message> outMessages = new ConcurrentLinkedQueue<Message>();
    private LinkedList<NetEventListener> listeners = new LinkedList<NetEventListener>();

    /**
     *
     */
    private static class Host extends Com {
        private final ServerSocket server;
        private final int port;

        public Host(int port) throws IOException {
            this.port = port;
            server = new ServerSocket(port);
        }

        @Override
        public void initialize() {
            try {
                server.setSoTimeout(DEFAULT_TIMEOUT);
                server.accept();
            } catch(SocketException e) {
                e.printStackTrace();
            } catch(SocketTimeoutException e) {
                e.printStackTrace();
            } catch(IOException e) {
                e.printStackTrace();
            }

            super.initialize();
        }
    }

    /**
     *
     */
    private static class Client extends Com {
        public Client(String address, int port) throws IOException {
            socket = new Socket(address, port);
        }
    }

    /**
     *
     * @param address
     * @param port
     * @return
     * @throws IOException
     */
    public static Com newClient(String address, int port) throws IOException {
        if(address.isEmpty() || (port < 1)) throw new IllegalArgumentException("");
        Client cl = new Client(address, port);
        cl.initialize();
        return cl;
    }

    /**
     *
     * @param port
     * @return
     * @throws IOException
     */
    public static Com newHost(int port) throws IOException {
        if(port < 1) throw new IllegalArgumentException("");
        Host h = new Host(port);
        h.initialize();
        return h;
    }

    /**
     *
     */
    protected void initialize() {
        try {
            outWorker = new OutWorker(socket.getOutputStream(), outMessages);
            inWorker = new InWorker(socket.getInputStream(), inMessages);
        } catch(IOException e) { e.printStackTrace(); }
    }

    /**
     *
     */
    public void start() {
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

    public Queue<Message> getPendingMessages() {
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
}

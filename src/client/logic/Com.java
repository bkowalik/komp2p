package client.logic;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

import agh.po.Msg;
import client.DLog;
import client.event.ConnectionEvent;
import client.event.ConnectionListener;
import client.event.MessageListener;
import client.exception.BadIdException;
import client.exception.BadPortException;
import client.exception.ComException;
import client.exception.ConnectionTimeoutException;
import client.exception.HostException;

/**
 * Common interface for communication classes.
 * @author Bartosz Kowalik
 */
public abstract class Com {
    /**
     * Validation max port number.
     */
    public static final int MAX_PORT = 65535;
    /**
     * Validation minumum port.
     */
    public static final int MIN_PORT = 0;
    /**
     * Default connection timeout.
     */
    public static final int DEFAULT_CONNECTION_TIMEOUT = 5000;
    /**
     * Default idle timeout.
     */
    public static final int DEFAULT_IDLE_TIMEOUT = 2000;
    /**
     * Default connection port.
     */
    public static final int DEFAULT_PORT = 44321;
    /**
     * Connection socket.
     */
    protected Socket socket;
    /**
     * Incomming messages thread.
     */
    protected InWorker inWorker;
    /**
     * Outcomming messages thread.
     */
    protected OutWorker outWorker;
    /**
     * Message dispatcher thread. It is unnecessary but offloads incomming messages thread.
     */
    protected Dispatcher dispatcher;
    protected String id;
    protected ExecutorService exec = Executors.newFixedThreadPool(3);
    
    private boolean running;
    protected int port;
    
    private final BlockingQueue<Msg> inMsgs = new LinkedBlockingQueue<Msg>();
    private final BlockingQueue<Msg> outMsgs = new LinkedBlockingQueue<Msg>();
    
    private final List<MessageListener> msgsListeners = new LinkedList<MessageListener>();
    private final Queue<ConnectionListener> conListener = new ConcurrentLinkedQueue<ConnectionListener>();

    public static Com newClient(String address, int port, int timeout, String id)
            throws ComException {
        Com.validateInitData(port, id);

        Client client = null;
        try {
            client = new Client(address, port, timeout, id);
        } catch (UnknownHostException e) {
            throw new HostException();
        } catch (IOException e) {
            DLog.warn(e.getMessage());
            throw new ComException(e.getMessage());
        }

//        try {
//            client.initialize();
//        } catch (SocketTimeoutException e) {
//            throw new ConnectionTimeoutException();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        client.start();

        new Thread(client).start();
        return client;
    }

    public static Com newHost(int port, int timeout, String id)
            throws ComException {
        Com.validateInitData(port, id);
        Host host = null;
        try {
            host = new Host(port, timeout, id);
        } catch (IOException e) {
            DLog.warn(e.getMessage());
            throw new ComException(e.getMessage());
        }

        new Thread(host).start();
        return host;
    }

    protected void initialize() throws IOException {
        if (socket == null)
            throw new NullPointerException();
        try {
            outWorker = new OutWorker(socket.getOutputStream(), outMsgs, conListener);
            inWorker = new InWorker(socket.getInputStream(), inMsgs, conListener);
        } catch (IOException e) {
            DLog.warn(e.getMessage());
            stop();
            throw new IOException(e.getCause());
        }
        dispatcher = new Dispatcher(inMsgs, msgsListeners);
    }

    private static void validateInitData(int port, String id)
            throws BadPortException, BadIdException {
        if ((port < Com.MIN_PORT) || (port > Com.MAX_PORT))
            throw new BadPortException();
        if (id.equals(""))
            throw new BadIdException();
    }

    public synchronized void start() {
        if(running) return;
        exec.execute(outWorker);
        exec.execute(inWorker);
        exec.execute(dispatcher);
        running = true;
    }

    public synchronized void stop() {
        if(!running) return;
        exec.shutdownNow();
        try {
            socket.close();
        } catch (IOException e) {
        }
        running = false;
    }

    public synchronized void addMessageListener(MessageListener lst) {
        msgsListeners.add(lst);
    }
    
    public synchronized void addConnectionListener(ConnectionListener lst) {
        conListener.add(lst);
    }
    
    public synchronized void fireConnectionEvent(ConnectionEvent event) {
//        if(!running) return;
        for(ConnectionListener c : conListener) {
            c.onConnectionEvent(event);
        }
    }
    
    public void writeMessage(String msg) {
        if(running)
            outMsgs.add(new Msg(id, msg));
    }

    public String getID() {
        return id;
    }

    public int getPort() {
        return port;
    }
}

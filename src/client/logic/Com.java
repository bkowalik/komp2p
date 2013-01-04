package client.logic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

import agh.po.Message;
import client.DLog;
import client.event.ConnectionEvent;
import client.event.ConnectionListener;
import client.event.MessageListener;
import client.exception.BadIdException;
import client.exception.BadPortException;
import client.exception.ComException;
import client.exception.ConnectionTimeoutException;
import client.exception.HostException;

public abstract class Com {
    public static final int MAX_PORT = 65535;
    public static final int MIN_PORT = 0;
    public static final int DEFAULT_CONNECTION_TIMEOUT = 30000;
    public static final int DEFAULT_IDLE_TIMEOUT = 2000;
    public static final int DEFAULT_PORT = 44321;
    
    protected Socket socket;
    protected InWorker inWorker;
    protected OutWorker outWorker;
    protected Dispatcher dispatcher;
    protected String id;
    protected ExecutorService exec = Executors.newFixedThreadPool(3);
    
    private boolean runnign;
    
    private final BlockingQueue<Message> inMessages = new LinkedBlockingQueue<Message>();
    private final BlockingQueue<Message> outMessages = new LinkedBlockingQueue<Message>();
    
    private final List<MessageListener> msgsListeners = new LinkedList<MessageListener>();
    final Queue<ConnectionListener> conListener = new ConcurrentLinkedQueue<ConnectionListener>();

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

        try {
            client.initialize();
        } catch (SocketTimeoutException e) {
            throw new ConnectionTimeoutException();
        } catch (IOException e) {
            e.printStackTrace();
        }

        client.start();
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

//        try {
//            host.initialize();
//        } catch (SocketTimeoutException e) {
//            try { host.server.close(); } catch (IOException e1) { DLog.warn(e.getMessage());  }
//            throw new ConnectionTimeoutException();
//        } catch (IOException e) {
//            DLog.warn(e.getMessage());
//            try { host.server.close(); } catch (IOException e1) { DLog.warn(e.getMessage());  }
//        }

        new Thread(host).start();
        return host;
    }

    protected void initialize() throws IOException {
        if (socket == null)
            throw new NullPointerException();
        try {
            outWorker = new OutWorker(socket.getOutputStream(), outMessages, conListener);
            inWorker = new InWorker(socket.getInputStream(), inMessages, conListener);
        } catch (IOException e) {
            DLog.warn(e.getMessage());
            stop();
            throw new IOException(e.getCause());
        }
        dispatcher = new Dispatcher(inMessages, msgsListeners);
    }

    private static void validateInitData(int port, String id)
            throws BadPortException, BadIdException {
        if ((port < Com.MIN_PORT) || (port > Com.MAX_PORT))
            throw new BadPortException();
        if (id.equals(""))
            throw new BadIdException();
    }

    public synchronized void start() {
        if(runnign) return;
        exec.execute(outWorker);
        exec.execute(inWorker);
        exec.execute(dispatcher);
        runnign = true;
        System.out.println("Jak jest: " + conListener.isEmpty());
        fireConnectionEvent(new ConnectionEvent(this, null, ConnectionEvent.Type.ConnectionEstablished));
    }

    public synchronized void stop() {
        if(!runnign) return;
        exec.shutdownNow();
        try {
            socket.close();
        } catch (IOException e) {
        }
        runnign = false;
    }

    public synchronized void addMessageListener(MessageListener lst) {
        msgsListeners.add(lst);
    }
    
    public synchronized void addConnectionListener(ConnectionListener lst) {
        conListener.add(lst);
    }
    
    public synchronized void fireConnectionEvent(ConnectionEvent event) {
        if(!runnign) return;
        for(ConnectionListener c : conListener) {
            c.onConnectionEvent(event);
        }
    }
    
    public void writeMessage(String msg) {
        if(runnign)
            outMessages.add(new Message(id, msg));
    }

    public String getID() {
        return id;
    }
}

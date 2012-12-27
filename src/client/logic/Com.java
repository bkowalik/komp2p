package client.logic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import agh.po.Message;
import client.DLog;
import client.event.ConnectionListener;
import client.event.MessageListener;
import client.exception.BadIdException;
import client.exception.BadPortException;
import client.exception.ComException;
import client.exception.ConnectionTimeoutException;
import client.exception.HostException;

public abstract class Com {
    static final int MAX_PORT = 65535;
    static final int MIN_PORT = 0;
    
    public static final int DEFAULT_TIMEOUT = 60000;
    public static final int DEFAULT_PORT = 44321;
    
    protected Socket socket;
    protected InWorker inWorker;
    protected OutWorker outWorker;
    protected Dispatcher dispatcher;
    protected String id;
    protected ExecutorService exec = Executors.newFixedThreadPool(3);
    
    private final BlockingQueue<Message> inMessages = new LinkedBlockingQueue<Message>();
    private final BlockingQueue<Message> outMessages = new LinkedBlockingQueue<Message>();
    
    private final List<MessageListener> msgsListeners = new LinkedList<MessageListener>();
    private final List<ConnectionListener> conListener = new LinkedList<ConnectionListener>();

    private static class Host extends Com {
        private final ServerSocket server;
        
        public Host(int port, int timeout, String id) throws IOException {
            this.id = id;
            server = new ServerSocket(port);
            server.setSoTimeout(timeout);
        }

        @Override
        protected void initialize() throws IOException {
            socket = server.accept();
            super.initialize();
        }
    }

    private static class Client extends Com {       
        public Client(String address, int port, int timeout, String id) throws IOException {
            this.id = id;
            socket = new Socket(address, port);
        }
    }

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
        
        try {
            host.initialize();
        } catch (SocketTimeoutException e) {
            throw new ConnectionTimeoutException();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
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
        exec.execute(outWorker);
        exec.execute(inWorker);
        exec.execute(dispatcher);
    }

    public synchronized void stop() {
        exec.shutdownNow();
        try {
            socket.close();
        } catch (IOException e) {
        }
    }

    public synchronized void addMessageListener(MessageListener lst) {
        msgsListeners.add(lst);
    }
    
    public synchronized void addConnectionListener(ConnectionListener lst) {
        conListener.add(lst);
    }
    
    public void writeMessage(String msg) {
        outMessages.add(new Message(id, msg));
    }

    public BlockingQueue<Message> getPendingMessages() {
        return inMessages;
    }

    public String getID() {
        return id;
    }
}

package client.logic;

import agh.po.Message;
import client.event.ConnectionEvent;
import client.event.ConnectionEvent.Type;
import client.event.ConnectionListener;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Queue;

/**
 * Przyjmuje wiadomości przychodzące
 */
public class InWorker implements Runnable /* Callable<Void> */{
    protected final Queue<Message> messages;
    protected final ObjectInputStream input;
    private final Queue<ConnectionListener> conListeners;

    public InWorker(InputStream in, Queue<Message> messages,
            Queue<ConnectionListener> cls) throws IOException {
        input = new ObjectInputStream(in);
        this.messages = messages;
        conListeners = cls;
    }

    @Override
    public void run() {
        fireConnectionEvent(new ConnectionEvent(this, null, Type.ConnectionEstablished));
        while (!Thread.interrupted()) {
            try {
                Message msg = null;
                Object obj = input.readObject();
                if (!(obj instanceof Message))
                    continue;
                msg = (Message) obj;
                messages.add(msg);
            } catch (SocketException e) {
//                e.printStackTrace();
                fireConnectionEvent(new ConnectionEvent(this, e.getMessage(),
                        Type.SocketException));
                break;
            } catch (SocketTimeoutException e) {
//                e.printStackTrace();
                fireConnectionEvent(new ConnectionEvent(this, e.getMessage(),
                        Type.TimeoutException));
                break;
            } catch (EOFException e) {
//                e.printStackTrace();
                fireConnectionEvent(new ConnectionEvent(this, e.getCause().getMessage(),
                        Type.EOFException));
                break;
            } catch (IOException e) {
//                e.printStackTrace();
                fireConnectionEvent(new ConnectionEvent(this, e.getMessage(),
                        Type.IOException));
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
        } catch (IOException ex) {
        }
    }

    protected synchronized void fireConnectionEvent(ConnectionEvent event) {
        for (ConnectionListener els : conListeners) {
            els.onConnectionEvent(event);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        input.close();
        super.finalize();
    }
}

package client.logic;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Queue;

import agh.po.Message;
import client.DLog;
import client.event.ConnectionEvent;
import client.event.ConnectionEvent.Type;
import client.event.ConnectionListener;
import client.exception.ConnectionClosedException;

/**
 * Przyjmuje wiadomości przychodzące
 */
public class InWorker implements Runnable /* Callable<Void> */{
    protected final Queue<Message> messages;
    protected final ObjectInputStream input;
    private final List<ConnectionListener> conListeners;

    public InWorker(InputStream in, Queue<Message> messages,
            List<ConnectionListener> cls) throws IOException {
        input = new ObjectInputStream(new BufferedInputStream(in));
        this.messages = messages;
        conListeners = cls;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Message msg = null;
                Object obj = input.readObject();
                if (!(obj instanceof Message))
                    continue;
                msg = (Message) obj;
                messages.add(msg);
            } catch (SocketException e) {
//                DLog.warn(e.getMessage());
                e.printStackTrace();
                fireConnectionEvent(new ConnectionEvent(this, e.getMessage(),
                        Type.SocketException));
                break;
            } catch (SocketTimeoutException e) {
                DLog.warn(e.getMessage());
                fireConnectionEvent(new ConnectionEvent(this, e.getMessage(),
                        Type.TimeoutException));
                break;
            } catch (EOFException e) {
                DLog.warn(e.getMessage());
                fireConnectionEvent(new ConnectionEvent(this, e.getMessage(),
                        Type.EOFException));
                break;
            } catch (IOException e) {
                e.printStackTrace();
                fireConnectionEvent(new ConnectionEvent(this, e.getMessage(),
                        Type.IOException));
                break;
            } catch (ClassNotFoundException e) {
                DLog.warn(e.getMessage());
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

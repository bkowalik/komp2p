package client.logic;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import agh.po.Message;
import client.event.ConnectionEvent;
import client.event.ConnectionEvent.Type;
import client.event.ConnectionListener;

public class OutWorker implements Runnable {
    private final ObjectOutputStream output;
    private final BlockingQueue<Message> messages;
    private final List<ConnectionListener> conListeners;

    public OutWorker(OutputStream out, BlockingQueue<Message> messages, List<ConnectionListener> cls)
            throws IOException {
        output = new ObjectOutputStream(new BufferedOutputStream(out));
        output.flush();
        this.messages = messages;
        conListeners = cls;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Message msg = messages.take();
                if (msg != null) {
                    try {
                        output.writeObject(msg);
                        output.flush();
                        output.reset();
                    } catch (SocketException e) {
                        e.printStackTrace();
                        fireConnectionEvent(new ConnectionEvent(this, e.getMessage(), Type.SocketException));
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                        fireConnectionEvent(new ConnectionEvent(this, e.getMessage(), Type.IOException));
                        break;
                    }
                }
            }
        } catch (InterruptedException e) {
        } finally {
            try { output.close(); } catch (IOException ex) {}
        }
    }

    protected synchronized void fireConnectionEvent(ConnectionEvent event) {
        for(ConnectionListener e : conListeners) {
            e.onConnectionEvent(event);
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        output.close();
        super.finalize();
    }
}

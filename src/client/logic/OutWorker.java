package client.logic;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import agh.po.Msg;
import client.event.ConnectionEvent;
import client.event.ConnectionEvent.Type;
import client.event.ConnectionListener;

public class OutWorker implements Runnable {
    private final ObjectOutputStream output;
    private final BlockingQueue<Msg> msgs;
    private final Queue<ConnectionListener> conListeners;

    public OutWorker(OutputStream out, BlockingQueue<Msg> msgs, Queue<ConnectionListener> cls)
            throws IOException {
        output = new ObjectOutputStream(new BufferedOutputStream(out));
        output.flush();
        this.msgs = msgs;
        conListeners = cls;
    }

    @Override
    public void run() {
//        fireConnectionEvent(new ConnectionEvent(this, null, Type.ConnectionEstablished));
        try {
            while (!Thread.interrupted()) {
                Msg msg = msgs.take();
                if (msg != null) {
                    try {
                        output.writeObject(msg);
                        output.flush();
                        output.reset();
                    } catch (SocketException e) {
//                        e.printStackTrace();
                        fireConnectionEvent(new ConnectionEvent(this, e.getMessage(), Type.SocketException));
                        break;
                    } catch (IOException e) {
//                        e.printStackTrace();
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
        System.out.println("Odpalam");
        for(ConnectionListener e : conListeners) {
            System.out.println("Jest listener");
            e.onConnectionEvent(event);
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        output.close();
        super.finalize();
    }
}

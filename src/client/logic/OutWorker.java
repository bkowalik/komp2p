package client.logic;


import agh.po.Message;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OutWorker implements Runnable {
    protected final ObjectOutputStream output;
    protected final ConcurrentLinkedQueue<Message> messages;

    public OutWorker(OutputStream out, ConcurrentLinkedQueue<Message> messages) throws IOException {
        output = new ObjectOutputStream(new BufferedOutputStream(out));
        output.flush();
        this.messages = messages;
    }

    @Override
    public void run() {
        while(!Thread.interrupted()) {
            Message msg = messages.poll();
            if(msg != null) {
                try {
                    output.writeObject(msg);
                    output.flush();
                    output.reset();
                }
                catch(SocketException e) {
                    e.printStackTrace();
                    break;
                }
                catch(IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
        try { output.close(); } catch(IOException ex) {}
    }

    protected void finalize() throws Throwable {
        output.close();
        super.finalize();
    }
}

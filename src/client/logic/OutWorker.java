package client.logic;


import agh.po.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OutWorker implements Runnable {
    protected final ObjectOutputStream output;
    protected final ConcurrentLinkedQueue<Message> messages;

    public OutWorker(OutputStream out, ConcurrentLinkedQueue<Message> messages) throws IOException {
        output = new ObjectOutputStream(out);
        this.messages = messages;
    }

    @Override
    public void run() {
        while(!Thread.interrupted()) {
            Message msg = messages.poll();
            if(msg != null) {
                try { output.writeObject(msg); }
                catch(SocketException e) {
                    break;
                }
                catch(IOException e) {
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

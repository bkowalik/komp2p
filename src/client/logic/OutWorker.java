package client.logic;


import agh.po.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OutWorker implements Runnable {
    protected final ObjectOutputStream output;
    protected ConcurrentLinkedQueue<Message> messages;

    public OutWorker(OutputStream out, ConcurrentLinkedQueue<Message> messages) throws IOException {
        output = new ObjectOutputStream(out);
        this.messages = messages;
    }

    @Override
    public void run() {
        Message msg = messages.poll();
        if(msg != null) {
            try { output.writeObject(msg); }
            catch(IOException e) { e.printStackTrace(); }
        }
    }

    protected void finalize() throws Throwable {
        output.close();
        super.finalize();
    }
}

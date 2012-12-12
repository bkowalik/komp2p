package client.logic;


import agh.po.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Przyjmuje wiadomości przychodzące
 */
public class InWorker implements Runnable {
    protected ConcurrentLinkedQueue<Message> messages;
    protected final ObjectInputStream input;

    public InWorker(InputStream in, ConcurrentLinkedQueue<Message> messages) throws IOException {
        input = new ObjectInputStream(in);
        this.messages = messages;
    }

    @Override
    public void run() {
        while(!Thread.interrupted()) {
            try {
                if(input.available() > 0) {
                    Message msg = (Message) input.readObject();
                    messages.add(msg);
                }
            }
            catch(IOException e) { e.printStackTrace(); }
            catch(ClassNotFoundException e) { e.printStackTrace(); }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        input.close();
        super.finalize();
    }
}

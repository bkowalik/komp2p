package client.logic;


import agh.po.Message;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Przyjmuje wiadomości przychodzące
 */
public class InWorker implements Runnable {
    protected final ConcurrentLinkedQueue<Message> messages;
    protected final ObjectInputStream input;

    public InWorker(InputStream in, ConcurrentLinkedQueue<Message> messages) throws IOException {
        input = new ObjectInputStream(in);
        this.messages = messages;
    }

    @Override
    public void run() {
        while(!Thread.interrupted()) {
            try {
                if(input.available() != -1) {
                    Message msg = null;
                    Object obj = input.readObject();
                    if(!(obj instanceof  Message)) continue;
                    msg = (Message) obj;
                    messages.add(msg);
                }
            }
            catch(EOFException e) {
                System.out.println("Zdalny host zakończył połączenie.");
                break;
            }
            catch(IOException e) {
                e.printStackTrace();
                break;
            }
            catch(ClassNotFoundException e) { e.printStackTrace(); }
        }
        try { input.close(); } catch(IOException ex) {}
    }

    @Override
    protected void finalize() throws Throwable {
        input.close();
        super.finalize();
    }
}

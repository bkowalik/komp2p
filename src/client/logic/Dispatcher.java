package client.logic;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import agh.po.Message;
import client.event.MessageEvent;
import client.event.MessageListener;

public class Dispatcher implements Runnable {

    private final BlockingQueue<Message> messages;
    private final List<MessageListener> listeners;
    
    public Dispatcher(BlockingQueue<Message> messages, List<MessageListener> listeners) {
        this.messages = messages;
        this.listeners = listeners;
    }
    
    @Override
    public void run() {
        Message msg;
        MessageEvent event;
        while(!Thread.interrupted()) {
            msg = null;
            event = null;
            try {
                msg = messages.take();
            } catch (InterruptedException e) { break; }
            event = new MessageEvent(this, msg);
            for(MessageListener lst : listeners)
                lst.onMessageReceived(event);
        }
    }

    protected synchronized void fireMessageEvent(MessageEvent event) {
        for(MessageListener m : listeners)
            m.onMessageReceived(event);
    }
}

package client.event;

import java.util.EventObject;

import agh.po.Message;

public class MessageEvent extends EventObject {

    private final Message msg;
    
    public MessageEvent(Object source, Message msg) {
        super(source);
        this.msg = msg;
    }
    
    public Message getMessage() {
        return msg;
    }
}

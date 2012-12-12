package agh.po;

import java.io.Serializable;

public class Message implements Serializable {
    public final String id; //id rozmówcy
    public final String msg; //treść wiadomości

    public Message(String id, String msg) {
        this.id = id;
        this.msg = msg;
    }
}

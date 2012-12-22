package agh.po;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 6957732562810464788L;
    public final String id; //id rozmówcy
    public final String msg; //treść wiadomości

    public Message(String id, String msg) {
        this.id = id;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return id + ": " + msg;
    }
}

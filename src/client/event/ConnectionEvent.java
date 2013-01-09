package client.event;

import java.util.EventObject;

public class ConnectionEvent extends EventObject {
    private final String cause;
    private final Type type;
    
    public static enum Type {
        TimeoutException, RemoteHostDisconnect, SocketException,
        EOFException, IOException, ConnectionEstablished, UnknownHost;
    }
    
    public ConnectionEvent(Object source, String cause, Type e) {
        super(source);
        this.cause = cause;
        type = e;
    }
    
    public String getCause() {
        return cause;
    }
    
    public Type getType() {
        return type;
    }
}

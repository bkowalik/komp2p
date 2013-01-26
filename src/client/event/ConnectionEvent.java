package client.event;

import java.util.EventObject;

/**
 *
 */
public class ConnectionEvent extends EventObject {
    /**
     *
     */
    private final String cause;
    /**
     *
     */
    private final Type type;

    /**
     * Common cause of connection event
     */
    public static enum Type {
        TimeoutException, RemoteHostDisconnect, SocketException,
        EOFException, IOException, ConnectionEstablished, UnknownHost;
    }

    /**
     *
     * @param source
     * @param cause String representation of event cause (used in Exceptions)
     * @param e Type representation of event (used in swtiches)
     */
    public ConnectionEvent(Object source, String cause, Type e) {
        super(source);
        this.cause = cause;
        type = e;
    }

    /**
     *
     * @return cause of event
     */
    public String getCause() {
        return cause;
    }

    /**
     *
     * @return type of event
     */
    public Type getType() {
        return type;
    }
}

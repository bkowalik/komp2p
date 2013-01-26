package agh.po;

import java.io.Serializable;

/**
 * Basic protocol class.
 */
public class Msg implements Serializable {
    private static final long serialVersionUID = -7287335892272393745L; //Msg
    /**
     * Identyficator of sending person
     */
    public final String id;
    /**
     * Message content.
     */
    public final String msg;

    /**
     * Allows to initialize message
     * @param id identification
     * @param msg message content
     */
    public Msg(String id, String msg) {
        this.id = id;
        this.msg = msg;
    }

    /**
     *
     * @return String representation of message
     */
    @Override
    public String toString() {
        return id + ": " + msg;
    }
}

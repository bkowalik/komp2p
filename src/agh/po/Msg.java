package agh.po;

import java.io.Serializable;

/**
 *
 */
public class Msg implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -7287335892272393745L; //Msg
//    private static final long serialVersionUID = 6957732562810464788L; //Message
    /**
     *
     */
    public final String id; //id rozmówcy
    /**
     *
     */
    public final String msg; //treść wiadomości

    /**
     *
     * @param id
     * @param msg
     */
    public Msg(String id, String msg) {
        this.id = id;
        this.msg = msg;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return id + ": " + msg;
    }
}

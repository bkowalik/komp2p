package client;

import java.util.logging.Logger;


public final class DLog {
    private static final Logger logger = Logger.getLogger("Debuger");
    public static boolean DEBUG = true;

    public static void info(String msg) {
        logger.info(msg);
    }
}

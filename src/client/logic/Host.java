package client.logic;

import client.DLog;
import client.event.ConnectionEvent;
import client.event.ConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

class Host extends Com implements Runnable {
    private final ServerSocket server;

    public Host(int port, int timeout, String id) throws IOException {
        this.id = id;
        server = new ServerSocket(port);
    }

    @Override
    protected void initialize() throws IOException {
        socket = server.accept();
        super.initialize();
    }

    @Override
    public void run() {
        try{
            initialize();
        } catch(SocketTimeoutException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
        start();

        for(ConnectionListener cls : conListener) cls.onConnectionEvent(new ConnectionEvent(this, null, ConnectionEvent.Type.ConnectionEstablished));
    }

    @Override
    public synchronized void stop() {
        super.stop();
        try {
            server.close();
        } catch (IOException e) { DLog.warn(e.getMessage()); }
    }
}

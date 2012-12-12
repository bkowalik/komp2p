package client;

import client.gui.MainWindow;
import client.logic.Host;

import javax.swing.*;
import java.io.IOException;

/**
 * 0 - Host
 * 1 - Client
 */
public class Main {

    public static void main(String[] args) {
//        try { runConsole(args); } catch(IOException e) { e.printStackTrace(); }
        runGui();
    }

    public static void runConsole(String[] args) throws IOException {
        if(args.length != 3) {
            System.out.println("Launching defualt host");
            Host host = new Host(44321);
            String id = "HOST";
            host.start();
            while(true) {
                System.out.println();
            }
        } else if(args.length == 3) {

        }
    }

    public static void runGui() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainWindow();
            }
        });
    }
}

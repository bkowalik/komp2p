package client;

import client.gui.MainWindow;

import javax.swing.*;
import java.io.IOException;

/**
 * 0 - Host
 * 1 - Client
 */
public class Main {

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainWindow();
            }
        });
    }
}

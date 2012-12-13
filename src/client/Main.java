package client;

import agh.po.Message;
import client.gui.MainWindow;
import client.logic.Client;
import client.logic.Host;

import javax.swing.*;
import java.io.IOException;
import java.util.Queue;
import java.util.Scanner;

/**
 * 0 - Host
 * 1 - Client
 */
public class Main {

    public static void main(String[] args) {
        try { runConsole(args); } catch(IOException e) { e.printStackTrace(); }
//        runGui();
    }

    public static void runConsole(String[] args) throws IOException {
        if(args.length == 0) {
            runHost(44321, "HOST");
        } else {
            if(args[0].toLowerCase().equals("host")) {
                int port = Integer.valueOf(args[1]);
                String id = args[2];
                runHost(port, id);
            } else if(args[0].toLowerCase().equals("client")) {
                String host = args[1];
                int port = Integer.valueOf(args[2]);
                String id = args[3];
                runClient(host, port, id);
            } else throw new IllegalArgumentException("Nie można tak");
        }
    }

    protected static void runClient(String host, int port, String id) throws IOException {
        System.out.println("Launching client");
        Client client = new Client(host, port);
        client.start();
        Scanner in = new Scanner(System.in);
        while(true) {
            System.out.println("Czekam na wiadomość...");
            System.out.println(id + ": ");
            String msg = in.nextLine();
            if(!msg.equals("")) {
                client.writeMessage(new Message(id, msg));
            }
            Queue<Message> q = client.getAllIncommingMsgs();
            while(!q.isEmpty()) System.out.println(q.poll());
            System.out.println();
        }
    }

    protected static void runHost(int port, String id) throws IOException {
        System.out.println("Launching host");
        Host host = new Host(port);
        host.start();
        Scanner in = new Scanner(System.in);
        while(true) {
            System.out.println("Czekam na wiadomość...");
            System.out.println(id + ": ");
            String msg = in.nextLine();
            if(!msg.equals("")) {
                host.writeMessage(new Message(id, msg));
            }
            Queue<Message> q = host.getAllIncommingMsgs();
            while(!q.isEmpty()) System.out.println(q.poll());
            System.out.println();
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

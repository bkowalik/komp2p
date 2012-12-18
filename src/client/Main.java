package client;

import agh.po.Message;
import client.gui.MainWindow;
import client.logic.AbstractCommunicator;
import client.logic.Client;
import client.logic.Host;

import javax.swing.*;
import java.io.IOException;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class Main {

    public static void main(String[] args) throws IOException {
        try { runConsole(args); } catch(IOException e) { e.printStackTrace(); }
//        runGui();
    }

    public static void runConsole(String[] args) throws IOException {
        AbstractCommunicator com;
        String id = "HOST";
        if(args.length == 0) {
            com = new Host(44321);
        } else {
            if(args[0].toLowerCase().equals("host")) {
                int port = Integer.valueOf(args[1]);
                id = args[2];
                com = new Host(port);
            } else if(args[0].toLowerCase().equals("client")) {
                String host = args[1];
                int port = Integer.valueOf(args[2]);
                id = args[3];
                com = new Client(host, port);
            } else throw new IllegalArgumentException("Nie można tak");
        }

        com.start();
        Scanner in = new Scanner(System.in);
        System.out.println("Odpalam czat");
        while(true) {
            System.out.println(id + ": ");
            String msg = "";
            if(in.hasNextLine())
                msg = in.nextLine();
            if(!msg.equals("")) {
                com.writeMessage(new Message(id, msg));
            }

            Queue<Message> q = com.getAllIncommingMsgs();
            if(!q.isEmpty()) System.out.println("Odczytuje wiadomości:");
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

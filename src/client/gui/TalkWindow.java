package client.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import client.DLog;
import client.event.ConnectionEvent;
import client.event.ConnectionListener;
import client.event.MessageEvent;
import client.event.MessageListener;
import client.logic.Com;

public class TalkWindow extends JFrame {
    private JTextArea textChat;
    private JTextArea textMsg;
    private final Com com;
    private static final int DEFAULT_WIDTH = 450;
    private static final int DEFAULT_HEIGHT = 350;
    private JButton btnSend;
    private JButton btnDiscon;
    private final JFrame parent;
    private final ConnectionListener conLst = new ConLstn();

    private class WindowEvents implements WindowListener {
        @Override
        public void windowActivated(WindowEvent e) {}

        @Override
        public void windowClosed(WindowEvent e) {}
        
        @Override
        public void windowClosing(WindowEvent e) {
            TalkWindow.this.com.stop();
            TalkWindow.this.parent.setVisible(true);
        }

        @Override
        public void windowDeactivated(WindowEvent e) {}

        @Override
        public void windowDeiconified(WindowEvent e) {}

        @Override
        public void windowIconified(WindowEvent e) {}

        @Override
        public void windowOpened(WindowEvent e) {}
    }
    
    private class MessageLstn implements MessageListener {
        @Override
        public void onMessageReceived(MessageEvent event) {
            textChat.append(event.getMessage().toString() + '\n');
        }
    }

    private class ConLstn implements ConnectionListener {
        @Override
        public void onConnectionEvent(ConnectionEvent event) {
            switch (event.getType()) {
            case ConnectionEstablished:
                DLog.info(ConnectionEvent.Type.ConnectionEstablished.toString());
                TalkWindow.this.textChat.append("SYSTEM: Połączenie ustanowione." + '\n');
                TalkWindow.this.textMsg.setEnabled(true);
                TalkWindow.this.btnSend.setEnabled(true);
                break;
            case RemoteHostDisconnect:
            case TimeoutException:
//            case SocketException:
            case EOFException:
                JOptionPane.showMessageDialog(TalkWindow.this,
                        "Rozmówca rozłączył się " + event.getType().toString(), "Informacja",
                        JOptionPane.INFORMATION_MESSAGE, null);
                TalkWindow.this.textMsg.setEnabled(false);
                TalkWindow.this.btnSend.setEnabled(false);
                com.stop();
                break;
            }
        }
    }

    public TalkWindow(JFrame parent, Com c, String who) {
        setTitle("Komunikator - rozmowa jako " + who);
        this.parent = parent;
        com = c;
        addWindowListener(new WindowEvents());

        JSplitPane splitPane = new JSplitPane();
        splitPane.setResizeWeight(0.7);
        splitPane.setEnabled(false);
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        getContentPane().add(splitPane, BorderLayout.CENTER);

        JSplitPane splitPane_1 = new JSplitPane();
        splitPane_1.setResizeWeight(1.0);
        splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setRightComponent(splitPane_1);

        JScrollPane scrollPane = new JScrollPane();
        splitPane_1.setLeftComponent(scrollPane);

        textMsg = new JTextArea();
        //textMsg.setEnabled(false);
        textMsg.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_ENTER) {
                    event.consume();
                    performSending();
                }
            }
        });
        scrollPane.setViewportView(textMsg);

        JPanel panel = new JPanel();
        splitPane_1.setRightComponent(panel);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        btnSend = new JButton("Wyślij");
        //btnSend.setEnabled(false);
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performSending();
            }
        });
        panel.add(btnSend);

        btnDiscon = new JButton("Rozłącz");
        btnDiscon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TalkWindow.this.btnSend.setEnabled(false);
                TalkWindow.this.textMsg.setEnabled(false);
                TalkWindow.this.com.stop();
            }
        });
        panel.add(btnDiscon);

        JScrollPane scrollPane_1 = new JScrollPane();
        splitPane.setLeftComponent(scrollPane_1);

        textChat = new JTextArea();
        textChat.setEditable(false);
        scrollPane_1.setViewportView(textChat);
        //textChat.append("SYSTEM: Oczekuje na połączenie."+'\n');

        com.addMessageListener(new MessageLstn());
//        com.addConnectionListener(conLst);

        setLocationByPlatform(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        DLog.info("Obiekt powstał");
    }

    private void performSending() {
        String msg = textMsg.getText();
        if(msg.equals("")) return;
        textMsg.setText("");
        textChat.append(TalkWindow.this.com.getID() + ": " + msg
                + '\n');
        com.writeMessage(msg);
    }

    public ConnectionListener getConnectionListener() {
        return conLst;
    }
}

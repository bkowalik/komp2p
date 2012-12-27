package client.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

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
    
    private class MessageLstn implements MessageListener {
        @Override
        public void onMessageReceived(MessageEvent event) {   
            textChat.append(event.getMessage().toString() + '\n');
        }
    }
    
    private class ConLstn implements ConnectionListener {
        @Override
        public void onConnectionEvent(ConnectionEvent event) {
            JOptionPane.showMessageDialog(TalkWindow.this, event.getCause(), "Error", JOptionPane.ERROR_MESSAGE, null);
            com.stop();
        }
    }
    
    public TalkWindow(Com c) {
        setTitle("Komunikator - rozmowa");
        com = c;
        com.addMessageListener(new MessageLstn());
        com.addConnectionListener(new ConLstn());
        com.start();
        JSplitPane splitPane = new JSplitPane();
        splitPane.setResizeWeight(0.7);
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        getContentPane().add(splitPane, BorderLayout.CENTER);
        
        JSplitPane splitPane_1 = new JSplitPane();
        splitPane_1.setResizeWeight(1.0);
        splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setRightComponent(splitPane_1);
        
        JScrollPane scrollPane = new JScrollPane();
        splitPane_1.setLeftComponent(scrollPane);
        
        textMsg = new JTextArea();
        textMsg.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                if(event.getKeyCode() == KeyEvent.VK_ENTER) {
                    event.consume();
                    String msg = textMsg.getText();
                    textMsg.setText("");
                    textChat.append(TalkWindow.this.com.getID() + ": " + msg + '\n');
                    com.writeMessage(msg);
                }
            }
        });
        scrollPane.setViewportView(textMsg);
        
        JPanel panel = new JPanel();
        splitPane_1.setRightComponent(panel);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        JButton btnWylij = new JButton("Wyślij");
        btnWylij.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: dodać
            }
        });
        panel.add(btnWylij);
        
        JButton btnRozcz = new JButton("Rozłącz");
        btnRozcz.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: dodać
                TalkWindow.this.dispose();
            }
        });
        panel.add(btnRozcz);
        
        JScrollPane scrollPane_1 = new JScrollPane();
        splitPane.setLeftComponent(scrollPane_1);
        
        textChat = new JTextArea();
        textChat.setEditable(false);
        scrollPane_1.setViewportView(textChat);
        
        setLocationByPlatform(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setVisible(true);
    }

}

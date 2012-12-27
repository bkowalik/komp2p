package client.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;

import client.DLog;
import client.exception.ComException;
import client.exception.ConnectionTimeoutException;
import client.exception.HostException;
import client.logic.Com;

public class MainWindow extends JFrame {
    private JTextField fieldNick;
    private JTextField fieldPort;
    private JTextField fieldHost;
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private JRadioButton swClient;
    private JRadioButton swHost;
    private JLabel lblHost;
    private JButton btnConnect;
    private JButton btnHost;
    
    private enum State {
        HOST, CLIENT;
    }
    
    public MainWindow() {
        setTitle("Komunikator");
        
        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        
        swClient = new JRadioButton("Klient");
        swClient.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switchGui(State.CLIENT);
            }
        });
        buttonGroup.add(swClient);
        
        swHost = new JRadioButton("Host");
        swHost.setSelected(true);
        swHost.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switchGui(State.HOST);
            }
        });
        buttonGroup.add(swHost);
        
        fieldNick = new JTextField();
        fieldNick.setColumns(10);
        
        fieldPort = new JTextField();
        fieldPort.setColumns(10);
        
        fieldHost = new JTextField();
        fieldHost.setColumns(10);
        
        JLabel lblNick = new JLabel("Nick");
        lblNick.setFont(new Font("Tahoma", Font.PLAIN, 14));
        
        JLabel lblPort = new JLabel("Port");
        lblPort.setFont(new Font("Tahoma", Font.PLAIN, 14));
        
        lblHost = new JLabel("Host");
        lblHost.setFont(new Font("Tahoma", Font.PLAIN, 14));
        
        btnConnect = new JButton("Połącz");
        btnConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                int port = 0;
                try {
                    if(fieldPort.getText().equals("") && fieldNick.getText().equals("") && fieldHost.getText().equals("")) {
                        JOptionPane.showMessageDialog(MainWindow.this, "Proszę wypełnić wszystkie pola", "Błąd", JOptionPane.WARNING_MESSAGE, null);
                        return;
                    }
                    port = Integer.parseInt(fieldPort.getText());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(MainWindow.this, "Niewłaściwy numer portu", "Błąd", JOptionPane.ERROR_MESSAGE, null);
                    return;
                }
                
                try {
                    final Com c = Com.newClient(fieldHost.getText(), port, Com.DEFAULT_TIMEOUT, fieldNick.getText()); 
                    SwingUtilities.invokeLater(new Runnable() { 
                        @Override
                        public void run() {
                            MainWindow.this.setVisible(false);
                            TalkWindow talkWindow = new TalkWindow(MainWindow.this, c, fieldNick.getText());                        
                        }
                    });
                } catch(HostException e) {
                    JOptionPane.showMessageDialog(MainWindow.this, "Host nieosiągalny", "Błąd", JOptionPane.ERROR_MESSAGE, null);
                } catch (ComException e) {
                    e.printStackTrace();
                }
            }
        });
        
        btnHost = new JButton("Hostuj");
        btnHost.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                int port = 0;
                try {
                    if(fieldPort.getText().equals("") || fieldNick.getText().equals("")) {
                        JOptionPane.showMessageDialog(MainWindow.this, "Proszę wypełnić wszystkie pola", "Błąd", JOptionPane.WARNING_MESSAGE, null);
                        return;
                    }
                    port = Integer.parseInt(fieldPort.getText());
                } catch(NumberFormatException e) {
                    JOptionPane.showMessageDialog(MainWindow.this, "Niewłaściwy numer portu", "Błąd", JOptionPane.ERROR_MESSAGE, null);
                    return;
                }

                try {  
                    final Com c = Com.newHost(port, Com.DEFAULT_TIMEOUT, fieldNick.getText());                     
//                    SwingUtilities.invokeLater(new Runnable() { 
//                        @Override
//                        public void run() {
                            MainWindow.this.setVisible(false);
                            new TalkWindow(MainWindow.this, c, fieldNick.getText());                        
//                        }
//                    });
                } catch (ComException e) {
                    e.printStackTrace();
                }
            }
        });
        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(
            gl_panel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel.createSequentialGroup()
                    .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_panel.createSequentialGroup()
                            .addGap(39)
                            .addComponent(btnConnect))
                        .addGroup(gl_panel.createSequentialGroup()
                            .addGap(18)
                            .addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
                                .addComponent(lblNick)
                                .addComponent(swClient)
                                .addComponent(lblPort)
                                .addComponent(lblHost))))
                    .addGap(2)
                    .addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
                        .addComponent(fieldNick, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(swHost)
                        .addComponent(fieldPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(fieldHost, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnHost))
                    .addContainerGap(44, Short.MAX_VALUE))
        );
        gl_panel.setVerticalGroup(
            gl_panel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_panel.createSequentialGroup()
                    .addGap(15)
                    .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(swClient)
                        .addComponent(swHost))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblNick)
                        .addComponent(fieldNick, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblPort)
                        .addComponent(fieldPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblHost)
                        .addComponent(fieldHost, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
                        .addComponent(btnConnect)
                        .addComponent(btnHost))
                    .addContainerGap())
        );
        panel.setLayout(gl_panel);
        switchGui(State.HOST);
        setLocationByPlatform(true);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    private void switchGui(State s) {
        switch(s) {
        case CLIENT:
            lblHost.setEnabled(true);
            fieldHost.setEnabled(true);
            btnHost.setEnabled(false);
            btnConnect.setEnabled(true);
            break;
        case HOST:
            lblHost.setEnabled(false);
            fieldHost.setEnabled(false);
            btnHost.setEnabled(true);
            btnConnect.setEnabled(false);
            break;
        }
    }
}

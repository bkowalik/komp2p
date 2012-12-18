package client.gui.panels;

import client.gui.MainWindow;

import javax.swing.*;

public class SettingsPanel extends JPanel {
    private JTextField nickName;
    private JTextField host;
    private JTextField port;
    private JButton connectBtn;
    private JButton hostBtn;

    public SettingsPanel() {
        setup();
    }

    private void setup() {
        JLabel hostLabel = new JLabel("Host");
        add(hostLabel);
        host = new JTextField(15);
        add(host);

        JLabel portLabel = new JLabel("Port");
        add(portLabel);
        port = new JTextField(7);
        add(port);

        JLabel nickLabel = new JLabel("Nick");
        add(nickLabel);
        nickName = new JTextField(15);
        add(nickName);

        connectBtn = new JButton("Połącz");
        add(connectBtn);

        hostBtn = new JButton("Hostuj");
        add(hostBtn);
    }
}

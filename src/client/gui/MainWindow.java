package client.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import client.gui.panels.SettingsPanel;

public class MainWindow extends JFrame {
    private SettingsPanel settingsPanel;
    final static int WINDOW_HEIGHT = 400;
    final static int WINDOW_WIDTH = 400;
    
    public MainWindow() {
    
        settingsPanel = new SettingsPanel();
        add(settingsPanel);
        
        setCentered();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    private void setCentered() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        final int x = (screen.width - WINDOW_WIDTH)/2;
        final int y = (screen.height - WINDOW_HEIGHT)/2;
        
        setBounds(x, y, WINDOW_WIDTH, WINDOW_HEIGHT);
    }
}

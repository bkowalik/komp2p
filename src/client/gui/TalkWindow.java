package client.gui;

import javax.swing.JFrame;

import client.gui.panels.TalkPanel;

public class TalkWindow extends JFrame {
    
    public TalkWindow() {
        add(new TalkPanel());
        
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setVisible(true);
    }
}

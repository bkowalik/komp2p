package client.gui;


import client.gui.panels.TalkPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TalkWindow extends JFrame {
    private TalkPanel talkPanel;

    public TalkWindow(String name) {
        super(name);

        talkPanel = new TalkPanel();
        add(talkPanel);
    }

    private JPanel setupBtns() {
        JPanel panel = new JPanel();
        GridLayout layout = new GridLayout(0,2);
        panel.setLayout(layout);

        JButton sendBtn = new JButton(Labels.SEND.toString());
        sendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO:
            }
        });
        add(sendBtn);

        JButton diconnectBnt = new JButton(Labels.DISCONNECT.toString());
        diconnectBnt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO:
            }
        });
        add(diconnectBnt);

        return panel;
    }

    private enum Labels {
        SEND("Wyślij"),
        DISCONNECT("Odłącz"),
        ;

        private Labels(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }

        private final String text;
    }
}

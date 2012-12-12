package client.gui.panels;


import sun.rmi.runtime.NewThreadAction;

import javax.swing.*;
import java.awt.*;

public class TalkPanel extends JPanel {
    private JSplitPane jSplitPane;
    private JTextArea message;
    private JTextArea log;

    public TalkPanel() {
        super(new BorderLayout());
        message = new JTextArea();
        log = new JTextArea();
        JScrollPane logPane = new JScrollPane(log, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JScrollPane messagePane = new JScrollPane(message, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, logPane, messagePane);
        jSplitPane.setResizeWeight(0.7);

        add(jSplitPane, BorderLayout.CENTER);
    }
}

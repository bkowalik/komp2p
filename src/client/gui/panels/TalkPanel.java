package client.gui.panels;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class TalkPanel extends JPanel {

    private JTextArea msgLog;
    private JTextArea msgTxt;
    
    public TalkPanel() {
        msgLog = new JTextArea();
        msgTxt = new JTextArea();
        
        JScrollPane scrollMsgLog = new JScrollPane(msgLog);
        
        JScrollPane scrollMsgTxt = new JScrollPane(msgTxt);
        
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        jSplitPane.add(scrollMsgLog);
        jSplitPane.add(scrollMsgTxt);
        
        add(jSplitPane);
    }

}

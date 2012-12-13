package client.gui;


import client.gui.menus.FileMenu;
import client.gui.menus.HelpMenu;
import client.gui.panels.SettingsPanel;
import client.gui.panels.TalkPanel;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    protected static final int WINDOW_HEIGHT = 400;
    protected static final int WINDOW_WIDTH = 400;
    private JMenuBar jMenuBar;
    private JTabbedPane jTabbedPane;
    private TalkPanel talkPanel;
    private SettingsPanel settingsPanel;
    private boolean talking;

    public MainWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jMenuBar = new JMenuBar();
        setJMenuBar(jMenuBar);
        initMenuBar();

        settingsPanel = new SettingsPanel();
        talkPanel = new TalkPanel();

        add(settingsPanel);
        switchGui();
        setCentered();
        setVisible(true);
    }

    protected final void setCentered() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        final int x = (screen.width - WINDOW_WIDTH)/2;
        final int y = (screen.height - WINDOW_HEIGHT)/2;

        setBounds(x, y, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    private void initMenuBar() {
        jMenuBar.add(new FileMenu("File"));
        jMenuBar.add(new HelpMenu("Help"));
    }

    private void switchGui() {
        if(talking) {

        } else {

        }
    }
}

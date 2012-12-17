package client.gui;


import client.gui.menus.FileMenu;
import client.gui.menus.HelpMenu;
import client.gui.panels.SettingsPanel;
import client.gui.panels.TalkPanel;
import client.logic.events.NetEventListener;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    public static final String APP_NAME = "Komunikator";
    protected static final int WINDOW_HEIGHT = 400;
    protected static final int WINDOW_WIDTH = 400;
    private JMenuBar jMenuBar;
    private SettingsPanel settingsPanel;

    public MainWindow() {
        super(MainWindow.APP_NAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jMenuBar = new JMenuBar();
        setJMenuBar(jMenuBar);
        initMenuBar();

        settingsPanel = new SettingsPanel();
        add(settingsPanel);
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
}

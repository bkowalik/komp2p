package client.gui;


import client.gui.menus.FileMenu;
import client.gui.menus.HelpMenu;
import client.gui.panels.SettingsPanel;
import client.gui.panels.TalkPanel;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    public static final String APP_NAME = "Komunikator";
    protected static final int WINDOW_HEIGHT = 400;
    protected static final int WINDOW_WIDTH = 400;
    private JMenuBar jMenuBar;
    private JTabbedPane jTabbedPane;
    private TalkPanel talkPanel;
    private SettingsPanel settingsPanel;

    public MainWindow() {
        super(MainWindow.APP_NAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jMenuBar = new JMenuBar();
        setJMenuBar(jMenuBar);
        initMenuBar();

        settingsPanel = new SettingsPanel();
        talkPanel = new TalkPanel();

        jTabbedPane = new JTabbedPane();
        jTabbedPane.add("Ustawienia", settingsPanel);
        jTabbedPane.add("Rozmowa", talkPanel);
        add(jTabbedPane);
        switchGui(GuiState.IDLE);
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

    void switchGui(GuiState state) {
        switch (state) {
            case TALKING:
                break;
            case LISTENING:
                break;
            case IDLE:
                jTabbedPane.setEnabledAt(0, true);
                jTabbedPane.setEnabledAt(1, false);
                break;
        }
    }
}

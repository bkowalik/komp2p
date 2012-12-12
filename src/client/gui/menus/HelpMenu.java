package client.gui.menus;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class HelpMenu extends JMenu {

    public HelpMenu(String title) {
        super(title);
        addAboutProgram();
    }

    private void addAboutProgram() {
        JMenuItem about = new JMenuItem("About program");

        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: dodać akcje
            }
        });

        add(about);
    }
}

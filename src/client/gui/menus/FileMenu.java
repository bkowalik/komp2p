package client.gui.menus;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileMenu extends JMenu {

    public FileMenu(String title) {
        super(title);

        addExitButton();
    }

    private void addExitButton() {
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        add(exit);
    }

}

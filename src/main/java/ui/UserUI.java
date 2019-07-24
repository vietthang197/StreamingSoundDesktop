package ui;

import javax.swing.*;

public class UserUI extends JFrame {
    public UserUI(String name, int width, int height) {
        super(name);
        setSize(width, height);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        setDefaultLookAndFeelDecorated(true);
    }
}

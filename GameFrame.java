import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class GameFrame extends JFrame {
    GameFrame(String username) {
        setTitle("Space Invaders Starter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GamePanel panel = new GamePanel();
        panel.currentUser = username;
        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
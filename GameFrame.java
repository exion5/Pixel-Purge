import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class GameFrame extends JFrame {
    GameFrame() {
        setTitle("Space Invaders Starter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new GamePanel());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
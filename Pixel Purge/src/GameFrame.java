import javax.swing.*;

class GameFrame extends JFrame { // creates the frame for the game
    GameFrame(String username) {
        setTitle("Pixel Purge");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GamePanel panel = new GamePanel();
        panel.currentUser = username;
        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
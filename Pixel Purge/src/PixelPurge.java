import java.awt.Frame;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;

public class PixelPurge {
    public static void main(String[] args) { // runs the game when its ready
        SwingUtilities.invokeLater(() -> launchLogin());
    }

    public static void launchLogin() { // launches the login page
        LoginPage lp = new LoginPage((Frame)null);
        lp.setVisible(true);
        if (lp.getLoggedIn()) {
            lp.dispose();
            launchStart(lp.getUser());
        } else {
            System.exit(0);
        }
    }

    public static void launchStart(String username) { // opens the start page
        StartPage start = new StartPage((Frame)null, username);
        start.setVisible(true);
        if (start.getStart()) {
            start.dispose();
            GameFrame game = new GameFrame(username); // opens the actual game when the start button is pressed
            game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            launchLogin();
        }
    }
}
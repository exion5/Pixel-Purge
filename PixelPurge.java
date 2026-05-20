import java.awt.Frame;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;

public class PixelPurge {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> launchLogin());
    }

    public static void launchLogin() {
        LoginPage lp = new LoginPage((Frame)null);
        lp.setVisible(true);
        if (lp.getLoggedIn()) {
            lp.dispose();
            launchStart(lp.getUser());
        } else {
            System.exit(0);
        }
    }

    public static void launchStart(String username) {
        StartPage start = new StartPage((Frame)null, username);
        start.setVisible(true);
        if (start.getStart()) {
            start.dispose();
            GameFrame game = new GameFrame(username);
            game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            System.exit(0);
        }
    }
}
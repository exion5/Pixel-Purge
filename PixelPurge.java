import javax.swing.SwingUtilities;
import javax.swing.JFrame;

public class PixelPurge {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> launchLogin());
    }

    public static void launchLogin() {
        LoginPage lp = new LoginPage(null);
        lp.setVisible(true);

        if (lp.getLoggedIn()) {
            lp.dispose();
            launchStart();
        } else {
            System.exit(0);
        }
    }

    public static void launchStart() {
        StartPage start = new StartPage(null);
        start.setVisible(true);
        if (start.getStart()) {
            start.dispose();
            GameFrame game = new GameFrame();
            game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            System.exit(0);
        }
    }
}
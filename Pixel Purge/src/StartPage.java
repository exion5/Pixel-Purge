import javax.swing.*;
import java.awt.*;
import java.nio.file.*;
import java.util.List;
 
public class StartPage extends JDialog {
    private boolean start = false;
 
    public StartPage(Frame owner, String username) {
        super(owner, "Pixel Purge", true);
        setModal(true);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) { 
                System.exit(0); 
            } 
        });
        setSize(600, 550);
        setLocationRelativeTo(null);
        setResizable(false);
 
        JPanel mainPanel = new JPanel(new BorderLayout()) { // main background panel
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                // Draw black background
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setOpaque(false);
 
        JLabel titleLabel = new JLabel("PIXEL PURGE", SwingConstants.CENTER); // creates title label
        try {
            Font retro = Font.createFont(Font.TRUETYPE_FONT, new java.io.File("PressStart2P-Regular.ttf")).deriveFont(32f);
            titleLabel.setFont(retro);
        } catch (Exception e) {
            titleLabel.setFont(new Font("Monospaced", Font.BOLD, 32));
        }
        titleLabel.setForeground(new Color(0, 255, 100));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        JLabel subLabel = new JLabel("- SPACE DEFENDER -", SwingConstants.CENTER); // subtitle
        subLabel.setFont(new Font("Monospaced", Font.BOLD, 13));
        subLabel.setForeground(new Color(100, 200, 255));
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        JPanel centerPanel = new JPanel(); // center panel for the buttons
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
 
        int hs = 0; // sets the high score to the user high score
        try {
            List<String> lines = Files.readAllLines(Paths.get("Registration.txt")); // looks for who is signed in
            for (int i = 0; i + 2 < lines.size(); i += 3) {
                if (lines.get(i).trim().equals(username)) {
                    hs = Integer.parseInt(lines.get(i + 2).trim());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel highScore = new JLabel(String.format("HIGH SCORE: %06d", hs), SwingConstants.CENTER); // changes the highscore label to the user high score
        highScore.setFont(new Font("Monospaced", Font.BOLD, 12));
        highScore.setForeground(new Color(255, 220, 0));
        highScore.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        String[] controls = { // controls text
            "LEFT RIGHT to MOVE    UP to SHOOT",
            "DESTROY ALL INVADERS",
            "COLLECT POWERUPS"
        };
        for (String ctrl : controls) { // places the controls onto the panel
            JLabel cl = new JLabel(ctrl, SwingConstants.CENTER);
            cl.setFont(new Font("Monospaced", Font.PLAIN, 10));
            cl.setForeground(new Color(180, 180, 180));
            cl.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPanel.add(cl);
            centerPanel.add(Box.createVerticalStrut(6));
        }
 
        centerPanel.add(Box.createVerticalStrut(25));
 
        JButton startB = makeRetroButton("▶  START GAME", new Color(0, 255, 100)); // start button for game
        startB.setAlignmentX(Component.CENTER_ALIGNMENT);
        startB.addActionListener(e -> {
            Sound.stopBgMusic();
            start = true;
            setVisible(false);
            dispose();
        });
 
        JButton quitBtn = makeRetroButton("✕  QUIT", new Color(255, 80, 80)); // exits the game
        quitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitBtn.addActionListener(e -> {
            start = false;
            setVisible(false);
            dispose();
        });

        JButton loginBtn = makeRetroButton("🔒  BACK TO LOGIN", new Color(0, 0, 255)); // sends user back to the login
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> {
            start = false;
            Sound.stopBgMusic();
            dispose();
        });
 
        centerPanel.add(startB);
        centerPanel.add(Box.createVerticalStrut(7));
        centerPanel.add(loginBtn);
        centerPanel.add(Box.createVerticalStrut(7));
        centerPanel.add(quitBtn);
 
        JLabel credits = new JLabel("ETHAN XIONG - ICS4U Game Project", SwingConstants.CENTER); // Ethan Xiong game credit
        credits.setFont(new Font("Monospaced", Font.PLAIN, 9));
        credits.setForeground(new Color(80, 80, 80));
 
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        bottomPanel.add(credits, BorderLayout.CENTER);
 
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.BLACK);
        wrapper.add(titleLabel, BorderLayout.AFTER_LAST_LINE);
 
        JPanel fullCenter = new JPanel(new BorderLayout());
        fullCenter.setBackground(Color.BLACK);
        fullCenter.add(subLabel, BorderLayout.NORTH);
        fullCenter.add(centerPanel, BorderLayout.CENTER);
 
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(Color.BLACK);
        rootPanel.add(titleLabel, BorderLayout.CENTER);
 
        // Use layered pane approach - simpler stacking
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));
        outerPanel.setBackground(Color.BLACK);
        outerPanel.setPreferredSize(new Dimension(580, 510));
        outerPanel.add(Box.createVerticalStrut(80));
        outerPanel.add(titleLabel);
        outerPanel.add(Box.createVerticalStrut(6));
        outerPanel.add(subLabel);
        outerPanel.add(Box.createVerticalStrut(20));
        outerPanel.add(highScore);
        outerPanel.add(centerPanel);
        outerPanel.add(Box.createVerticalGlue());
        outerPanel.add(bottomPanel);
 
        JLayeredPane layered = new JLayeredPane();
        layered.setPreferredSize(new Dimension(580, 510));

        outerPanel.setBounds(0, 0, 580, 510);
        layered.add(outerPanel, JLayeredPane.DEFAULT_LAYER);

        JButton nextBtn = makeRetroButton("⏭  NEXT SONG", new Color(255, 180, 0));
        nextBtn.setMaximumSize(new Dimension(110, 20));
        nextBtn.setFont(new Font("Monospaced", Font.BOLD, 10));

        JSlider volumeSlider = new JSlider(0, 100, 75);
        volumeSlider.setMaximumSize(new Dimension(110, 26));
        volumeSlider.setOpaque(false);
        volumeSlider.addChangeListener(e -> Sound.setVolume(volumeSlider.getValue() / 100f));
        nextBtn.addActionListener(e -> Sound.nextTrack());

        JPanel musicPanel = new JPanel();
        musicPanel.setLayout(new BoxLayout(musicPanel, BoxLayout.Y_AXIS));
        musicPanel.setOpaque(false);
        nextBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        volumeSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        musicPanel.add(nextBtn);
        musicPanel.add(volumeSlider);
        musicPanel.setBounds(455, 8, 120, 70);
        layered.add(musicPanel, JLayeredPane.PALETTE_LAYER);

        add(layered);
        Sound.playPlaylist(new String[]{"Pixel Purge/Audio/Jasmine.wav", "Pixel Purge/Audio/The Color Violet.wav"});
    }
 
    private JButton makeRetroButton(String text, Color accent) { // creates the retro style buttons (gui design inspired by arcade games)
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(accent.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 40));
                } else {
                    g2.setColor(Color.BLACK);
                }
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(accent);
                g2.setStroke(new BasicStroke(2));
                g2.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
                g2.setColor(getModel().isRollover() ? accent : Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), tx, ty);
            }
        };
        btn.setFont(new Font("Monospaced", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(260, 44));
        btn.setMaximumSize(new Dimension(260, 44));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
 
    public boolean getStart() { // returns to see if the game has actually started yet
        return start;
    }
}
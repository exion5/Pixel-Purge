import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
 
public class StartPage extends JDialog {
    private boolean start = false;
 
    public StartPage(Frame owner) {
        super(owner, "Pixel Purge", true);
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(580, 510);
        setLocationRelativeTo(null);
        setResizable(false);
 
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                // Starfield background
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 80));
                long seed = 42;
                for (int i = 0; i < 80; i++) {
                    seed = seed * 6364136223846793005L + 1442695040888963407L;
                    int sx = (int) Math.abs(seed % getWidth());
                    seed = seed * 6364136223846793005L + 1442695040888963407L;
                    int sy = (int) Math.abs(seed % getHeight());
                    int size = (i % 3 == 0) ? 2 : 1;
                    g2.fillOval(sx, sy, size, size);
                }
            }
        };
        mainPanel.setOpaque(false);
 
        // Title panel
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());
 
                // Draw small pixel invaders as decoration
                drawPixelInvader(g2, 30, 30, new Color(0, 255, 100));
                drawPixelInvader(g2, 490, 30, new Color(0, 255, 100));
                drawPixelInvader(g2, 60, 60, new Color(100, 200, 255));
                drawPixelInvader(g2, 460, 60, new Color(100, 200, 255));
            }
 
            private void drawPixelInvader(Graphics2D g, int px, int py, Color c) {
                int[][] shape = {
                    {0,0,1,0,0,0,1,0,0},
                    {0,0,0,1,1,1,0,0,0},
                    {0,0,1,1,1,1,1,0,0},
                    {0,1,1,0,1,0,1,1,0},
                    {0,1,1,1,1,1,1,1,0},
                    {0,0,1,0,0,0,1,0,0},
                    {0,1,0,0,0,0,0,1,0},
                };
                g.setColor(c);
                int ps = 4;
                for (int row = 0; row < shape.length; row++)
                    for (int col = 0; col < shape[row].length; col++)
                        if (shape[row][col] == 1)
                            g.fillRect(px + col * ps, py + row * ps, ps, ps);
            }
        };
        titlePanel.setPreferredSize(new Dimension(580, 120));
        titlePanel.setOpaque(false);
 
        // Title label
        JLabel titleLabel = new JLabel("PIXEL PURGE", SwingConstants.CENTER);
        try {
            Font retro = Font.createFont(Font.TRUETYPE_FONT, new java.io.File("PressStart2P-Regular.ttf")).deriveFont(32f);
            titleLabel.setFont(retro);
        } catch (Exception e) {
            titleLabel.setFont(new Font("Monospaced", Font.BOLD, 32));
        }
        titleLabel.setForeground(new Color(0, 255, 100));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(130, 0, 0, 0));
 
        // Subtitle
        JLabel subLabel = new JLabel("- SPACE DEFENDER -", SwingConstants.CENTER);
        subLabel.setFont(new Font("Monospaced", Font.BOLD, 13));
        subLabel.setForeground(new Color(100, 200, 255));
 
        // Center panel with buttons
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
 
        // High score display
        JLabel highScore = new JLabel("INSERT COIN TO PLAY", SwingConstants.CENTER);
        highScore.setFont(new Font("Monospaced", Font.BOLD, 12));
        highScore.setForeground(new Color(255, 220, 0));
        highScore.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        // Blinking animation for high score label
        Timer blink = new Timer(600, e -> {
            highScore.setVisible(!highScore.isVisible());
        });
        blink.start();
 
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(highScore);
        centerPanel.add(Box.createVerticalStrut(30));
 
        // Controls info
        String[] controls = {
            "← → MOVE    SPACE SHOOT",
            "DESTROY ALL INVADERS",
            "COLLECT POWERUPS"
        };
        for (String ctrl : controls) {
            JLabel cl = new JLabel(ctrl, SwingConstants.CENTER);
            cl.setFont(new Font("Monospaced", Font.PLAIN, 10));
            cl.setForeground(new Color(180, 180, 180));
            cl.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPanel.add(cl);
            centerPanel.add(Box.createVerticalStrut(6));
        }
 
        centerPanel.add(Box.createVerticalStrut(25));
 
        // Start button
        JButton startBtn = makeRetroButton("▶  START GAME", new Color(0, 255, 100));
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        startBtn.addActionListener(e -> {
            blink.stop();
            start = true;
            setVisible(false);
            dispose();
        });
 
        // Quit button
        JButton quitBtn = makeRetroButton("✕  QUIT", new Color(255, 80, 80));
        quitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitBtn.addActionListener(e -> {
            blink.stop();
            start = false;
            setVisible(false);
            dispose();
        });
 
        centerPanel.add(startBtn);
        centerPanel.add(Box.createVerticalStrut(14));
        centerPanel.add(quitBtn);
 
        // Bottom credits
        JLabel credits = new JLabel("© 2025  PIXEL PURGE STUDIOS", SwingConstants.CENTER);
        credits.setFont(new Font("Monospaced", Font.PLAIN, 9));
        credits.setForeground(new Color(80, 80, 80));
 
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        bottomPanel.add(credits, BorderLayout.CENTER);
 
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.BLACK);
        wrapper.add(titlePanel, BorderLayout.NORTH);
        wrapper.add(titleLabel, BorderLayout.AFTER_LAST_LINE);
 
        JPanel fullCenter = new JPanel(new BorderLayout());
        fullCenter.setBackground(Color.BLACK);
        fullCenter.add(subLabel, BorderLayout.NORTH);
        fullCenter.add(centerPanel, BorderLayout.CENTER);
 
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(Color.BLACK);
        rootPanel.add(titlePanel, BorderLayout.NORTH);
        rootPanel.add(titleLabel, BorderLayout.CENTER);
 
        // Use layered pane approach - simpler stacking
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));
        outerPanel.setBackground(Color.BLACK);
        outerPanel.add(titlePanel);
        outerPanel.add(titleLabel);
        outerPanel.add(subLabel);
        outerPanel.add(centerPanel);
        outerPanel.add(bottomPanel);
 
        add(outerPanel);
    }
 
    private JButton makeRetroButton(String text, Color accent) {
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
 
    public boolean getStart() {
        return start;
    }
}
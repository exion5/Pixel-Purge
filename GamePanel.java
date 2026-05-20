import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
 
class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener {
 
    Player player;
    ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<Bullet> bullets = new ArrayList<>();
    ArrayList<ShieldBlock> shields = new ArrayList<>();
    ArrayList<Powerup> powerup = new ArrayList<>();
    int score = 0;
    int lives = 3;
    int level = 1;

    String gameState = "playing"; // playing, gameover, win
 
    Font retroLarge = null;
    Font retroSmall = null;
    Timer timer;

    private int hudPulse = 0;
    private boolean hudPulseUp = true;
 
    GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
 
        try {
            retroLarge = Font.createFont(Font.TRUETYPE_FONT, new File("PressStart2P-Regular.ttf"))
                             .deriveFont(Font.BOLD, 20f);
            retroSmall = retroLarge.deriveFont(Font.BOLD, 12f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            retroLarge = new Font("Monospaced", Font.BOLD, 20);
            retroSmall = new Font("Monospaced", Font.BOLD, 12);
        }
 
        player = new Player(380, 520);
        spawnEnemies();
        buildShields();
 
        timer = new Timer(16, this);
        timer.start();
 
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
    }
 
    void spawnEnemies() {
        enemies.clear();
        int rows = Math.min(3 + (level - 1), 5);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < 8; col++) {
                Enemy e = new Enemy(50 + col * 60, 50 + row * 40);
                e.dx = 2 + (level - 1);
                e.row = row; // store row for different colors
                enemies.add(e);
            }
        }
    }
 
    void buildShields() {
        shields.clear();
        for (int i = 0; i < 4; i++) {
            int baseX = 150 + i * 150;
            for (int j = 0; j < 5; j++) {
                shields.add(new ShieldBlock(baseX + j * 20, 450));
            }
        }
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameState.equals("playing")) {
            updateGame();
        }
        // Always update HUD pulse
        if (hudPulseUp) { hudPulse++; if (hudPulse >= 10) hudPulseUp = false; }
        else             { hudPulse--; if (hudPulse <= 0)  hudPulseUp = true;  }
        repaint();
    }
 
    void updateGame() {
        player.update();
 
        if (!enemies.isEmpty() && Math.random() < 0.015 + (level * 0.003)) { // enemy shooting
            int i = (int)(Math.random() * enemies.size());
            Enemy shooting = enemies.get(i);
            bullets.add(new Bullet(shooting.x + 15, shooting.y + 20, 4 + level));
        }
 
        boolean changeDirection = false; // updates enemy positioning
        for (Enemy enemy : enemies) {
            enemy.update();
            if (enemy.x < 0 || enemy.x > 760) changeDirection = true;
        }
 
        if (changeDirection) {
            for (Enemy enemy : enemies) {
                enemy.dx *= -1;
                enemy.y += 20;
                // If enemies reach player level
                if (enemy.y > 500) {
                    gameState = "gameover";
                    timer.stop();
                    return;
                }
            }
        }
 
        for (Bullet b : bullets) b.update();
        for (Powerup p : powerup) p.update();
 
        checkCollisions();
 
        bullets.removeIf(b -> !b.alive);
        enemies.removeIf(e -> !e.alive);
        shields.removeIf(s -> !s.alive);
        powerup.removeIf(p -> !p.alive);

        if (enemies.isEmpty()) { // win condition
            level++;
            if (level > 5) {
                gameState = "win";
                timer.stop();
            } else {
                spawnEnemies();
                buildShields();
            }
        }
 
        if (lives <= 0) {
            gameState = "gameover";
            timer.stop();
        }
    }
 
    void checkCollisions() {
        Rectangle pr = player.getBounds();
 
        for (Bullet b : bullets) {
            Rectangle br = b.getBounds();
 
            if (b.dy < 0) { // player bullet hits enemy
                for (Enemy e : enemies) {
                    if (br.intersects(e.getBounds())) {
                        e.alive = false;
                        b.alive = false;
                        score += 100 * level;
 
                        if (Math.random() < 0.1) {
                            String power = Math.random() < 0.5 ? "Shield" : "Life";
                            powerup.add(new Powerup(e.x, e.y, power));
                        }
                    }
                }
            }
 
            if (b.dy > 0) { // enemy bullet hits player
                if (br.intersects(pr)) {
                    b.alive = false;
                    lives--;
                    player.x = 380;
                    player.y = 520;
                }
            }
 
            for (ShieldBlock s : shields) {
                if (br.intersects(s.getBounds())) {
                    s.health--;
                    b.alive = false;
                    if (s.health <= 0) s.alive = false;
                }
            }
        }
 
        for (Powerup p : powerup) {
            if (p.getBounds().intersects(player.getBounds())) {
                p.alive = false;
                if (p.type.equals("Life")) {
                    lives++;
                } else if (p.type.equals("Shield")) {
                    buildShields();
                }
            }
        }
    }
 
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 
        // Starfield
        drawStarfield(g2);
 
        if (gameState.equals("playing")) {
            // Draw game objects
            player.draw(g2);
            for (Enemy enemy : enemies) enemy.draw(g2);
            for (Bullet bullet : bullets) bullet.draw(g2);
            for (ShieldBlock s : shields) s.draw(g2);
            for (Powerup p : powerup) p.draw(g2);
 
            drawHUD(g2);
        } else if (gameState.equals("gameover")) {
            drawOverlay(g2, "GAME OVER", new Color(255, 60, 60),
                "SCORE: " + score, "PRESS R TO RESTART");
        } else if (gameState.equals("win")) {
            drawOverlay(g2, "YOU WIN!", new Color(0, 255, 100),
                "FINAL SCORE: " + score, "PRESS R TO RESTART");
        }
    }
 
    void drawStarfield(Graphics2D g2) {
        g2.setColor(new Color(255, 255, 255, 50));
        long seed = 12345;
        for (int i = 0; i < 60; i++) {
            seed = seed * 6364136223846793005L + 1442695040888963407L;
            int sx = (int) Math.abs(seed % 800);
            seed = seed * 6364136223846793005L + 1442695040888963407L;
            int sy = (int) Math.abs(seed % 600);
            int size = (i % 5 == 0) ? 2 : 1;
            g2.fillOval(sx, sy, size, size);
        }
    }
 
    void drawHUD(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 180)); // background bar
        g2.fillRect(0, 0, 800, 48);
        g2.setColor(new Color(0, 255, 100, 80));
        g2.setStroke(new BasicStroke(1));
        g2.drawLine(0, 48, 800, 48);
 
        g2.setFont(retroSmall); // score
        g2.setColor(new Color(255, 220, 0));
        g2.drawString("SCORE", 20, 20);
        g2.setColor(Color.WHITE);
        g2.setFont(retroLarge);
        g2.drawString(String.format("%06d", score), 20, 42);
 
        g2.setFont(retroSmall); // sets level
        g2.setColor(new Color(100, 200, 255));
        String levelStr = "LEVEL " + level;
        int lw = g2.getFontMetrics().stringWidth(levelStr);
        g2.drawString(levelStr, (800 - lw) / 2, 30);

        g2.setFont(retroSmall); // shows lives as hearts
        g2.setColor(new Color(180, 180, 180));
        g2.drawString("LIVES", 620, 20);
 
        int heartX = 620;
        for (int i = 0; i < 5; i++) {
            if (i < lives) { // pulsates when down to last life
                Color heartColor = (lives <= 1 && i == 0)
                    ? new Color(255, 50 + hudPulse * 8, 50 + hudPulse * 8)
                    : new Color(255, 60, 80);
                drawHeart(g2, heartX + i * 30, 26, 14, heartColor);
            } else { // creates empty heart
                drawHeart(g2, heartX + i * 30, 26, 14, new Color(60, 60, 60));
            }
        }
        g2.setColor(new Color(0, 255, 100, 40));
        g2.drawLine(0, 570, 800, 570);
    }
 
    void drawHeart(Graphics2D g2, int cx, int cy, int size, Color c) { // creates the heart
        g2.setColor(c);
        int s = size / 4;
        g2.fillRect(cx + s,       cy,           s * 2, s);
        g2.fillRect(cx + s * 3,   cy,           s * 2, s);
        g2.fillRect(cx,           cy + s,       s * 6, s * 2);
        g2.fillRect(cx + s,       cy + s * 3,   s * 4, s);
        g2.fillRect(cx + s * 2,   cy + s * 4,   s * 2, s);
    }
 
    void drawOverlay(Graphics2D g2, String title, Color titleColor,
                     String sub1, String sub2) { // UI Changes
        g2.setColor(new Color(0, 0, 0, 200)); // dims background
        g2.fillRect(0, 0, 800, 600);
 
        int cw = 480, ch = 220;
        int cx = (800 - cw) / 2, cy = (600 - ch) / 2;
        g2.setColor(new Color(10, 10, 10));
        g2.fillRect(cx, cy, cw, ch);
        g2.setColor(titleColor);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(cx, cy, cw, ch);
        g2.setColor(new Color(titleColor.getRed(), titleColor.getGreen(), titleColor.getBlue(), 60));
        g2.drawRect(cx + 4, cy + 4, cw - 8, ch - 8);
        g2.setFont(retroLarge);
        g2.setColor(titleColor);
        FontMetrics fm = g2.getFontMetrics();
        int tx = cx + (cw - fm.stringWidth(title)) / 2;
        g2.drawString(title, tx, cy + 70);
        g2.setFont(retroSmall);
        g2.setColor(Color.WHITE);
        fm = g2.getFontMetrics();
        g2.drawString(sub1, cx + (cw - fm.stringWidth(sub1)) / 2, cy + 115);
        if ((System.currentTimeMillis() / 500) % 2 == 0) {
            g2.setColor(new Color(200, 200, 200));
            g2.drawString(sub2, cx + (cw - fm.stringWidth(sub2)) / 2, cy + 155);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) { // checks player input
        if (e.getKeyCode() == KeyEvent.VK_LEFT)  player.movingLeft  = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.movingRight = true;
        if (e.getKeyCode() == KeyEvent.VK_SPACE && gameState.equals("playing")) {
            bullets.add(new Bullet(player.x + 18, player.y, -8));
        }
        if (e.getKeyCode() == KeyEvent.VK_R && !gameState.equals("playing")) {
            restartGame();
        }
    }
 
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT)  player.movingLeft  = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.movingRight = false;
    }
 
    @Override public void keyTyped(KeyEvent e) {}
 
    @Override
    public void mouseClicked(MouseEvent e) {
        if (gameState.equals("playing"))
            bullets.add(new Bullet(player.x + 18, player.y, -8));
    }
 
    void restartGame() {
        score = 0;
        lives = 3;
        level = 1;
        bullets.clear();
        powerup.clear();
        player.x = 380;
        player.y = 520;
        player.movingLeft = false;
        player.movingRight = false;
        spawnEnemies();
        buildShields();
        gameState = "playing";
        timer.start();
    }
 
    @Override public void mousePressed(MouseEvent e)  {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e)  {}
    @Override public void mouseExited(MouseEvent e)   {}
}
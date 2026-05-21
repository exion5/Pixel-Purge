import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
 
class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener {
 
    Player player;
    ArrayList<Enemy> enemies = new ArrayList<>();
    ArrayList<Bullet> bullets = new ArrayList<>();
    ArrayList<ShieldBlock> shields = new ArrayList<>();
    ArrayList<Powerup> powerup = new ArrayList<>();
    int score = 0;
    int lives = 3;
    int level = 1;
    Rectangle menu = null;
    String currentUser = "";

    String gameState = "playing"; // playing, gameover, win
 
    Font retroLarge = null;
    Font retroSmall = null;
    Timer timer;
 
    GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
 
        try { // creates the retro font
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
 
        timer = new Timer(16, this); // tries to cap the game at roughly 60 FPS, 1000ms / 60 = 16.67ms per frame
        timer.start();
 
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
        Sound.playPlaylist(new String[]{"Pixel Purge/Audio/Reminder.wav"}); // background music
    }
 
    void spawnEnemies() { // spawns enemies in a grid formation, increases as levels go up
        enemies.clear();
        int rows = Math.min(3 + (level - 1), 5);
        for (int row = 0; row < rows; row++) {
            for (int i = 0; i < 8; i++) {
                Enemy e = new Enemy(50 + i * 60, 50 + row * 40);
                e.dx = 2 + (level - 1);
                e.row = row; // store row for different colors
                enemies.add(e);
            }
        }
    }
 
    void buildShields() { // creates the shield in front of the player
        shields.clear();
        for (int i = 0; i < 4; i++) {
            int baseX = 150 + i * 150;
            for (int j = 0; j < 5; j++) {
                shields.add(new ShieldBlock(baseX + j * 20, 450));
            }
        }
    }
 
    @Override
    public void actionPerformed(ActionEvent e) { // allows game to continuously update and repaint
        if (gameState.equals("playing")) updateGame();
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
 
        if (changeDirection) { // if any enemy hits the wall, they all change direction and move down
            for (Enemy enemy : enemies) {
                enemy.dx *= -1;
                enemy.y += 20;
                if (enemy.y > 500) { // if the enemies reach the botton, the player loses
                    Sound.stopBgMusic();
                    gameState = "gameover";
                    saveHighScore();
                    timer.stop();
                    return;
                }
            }
        }
 
        for (Bullet b : bullets){
            b.update();
        }
        for (Powerup p : powerup){
            p.update();
        }
 
        checkCollisions();
 
        bullets.removeIf(b -> !b.alive);
        enemies.removeIf(e -> !e.alive);
        shields.removeIf(s -> !s.alive);
        powerup.removeIf(p -> !p.alive);

        if (enemies.isEmpty()) { // win condition
            level++;
            Sound.sfx("Pixel Purge/Audio/beatLevel.wav");
            if (level > 5) {
                gameState = "win";
                Sound.sfx("Pixel Purge/Audio/win.wav");
                saveHighScore();
                timer.stop();
            } else {
                spawnEnemies();
                buildShields();
            }
        }
 
        if (lives <= 0) {
            gameState = "gameover";
            Sound.sfx("Pixel Purge/Audio/loss.wav");
            saveHighScore();
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
                        Sound.sfx("Pixel Purge/Audio/shot.wav");
 
                        if (Math.random() < 0.05) { // gives the player a 5% chance to get a powerup when they kill an enemy
                            String[] powers = {"Shield", "Life"}; // two types of powerups, one gives the player an extra life, the other repairs the shields
                            String power = "";
                            if (lives >= 3){
                                power = "Shield";
                            } else {
                                power = powers[(int)(Math.random() * 2)]; // randomly selects a powerup
                            }
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
                    Sound.sfx("Pixel Purge/Audio/explosion.wav");
                }
            }
 
            for (ShieldBlock s : shields) { // bullets hit the shield blocks, which have 3 health and can be repaired with powerups
                if (br.intersects(s.getBounds())) {
                    s.health--;
                    b.alive = false;
                    Sound.sfx("Pixel Purge/Audio/shieldHit.wav");
                    if (s.health <= 0) s.alive = false;
                }
            }
        }
 
        for (Powerup p : powerup) { // player collects powerups by touching them, checks if player is already at max lives before giving life powerup
            if (p.getBounds().intersects(player.getBounds())) {
                p.alive = false;
                if (p.type.equals("Life") && lives < 3) {
                    lives++;
                    Sound.sfx("Pixel Purge/Audio/heal.wav"); // life powerup sound
                } else if (p.type.equals("Shield")) {
                    buildShields();
                    Sound.sfx("Pixel Purge/Audio/coin.wav"); // shield powerup sound
                }
            }
        }
    }
 
    @Override
    public void paintComponent(Graphics g) { // draws everything on the screen, from the player and enemies to the background and UI
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 
        if (gameState.equals("playing")) {
            player.draw(g2); // draws the player, enemies, bullets, shields, and powerups
            for (Enemy enemy : enemies){
                enemy.draw(g2);
            }
            for (Bullet bullet : bullets){
                bullet.draw(g2);
            }
            for (ShieldBlock s : shields){
                s.draw(g2);
            }
            for (Powerup p : powerup){
                p.draw(g2);
            }
 
            drawHUD(g2);
        } else if (gameState.equals("gameover")) {
            drawOverlay(g2, "GAME OVER", new Color(255, 60, 60), "SCORE: " + score, "PRESS R TO RESTART");
        } else if (gameState.equals("win")) {
            drawOverlay(g2, "YOU WIN!", new Color(0, 255, 100), "FINAL SCORE: " + score, "PRESS R TO RESTART");
        }
    }
 
    void drawHUD(Graphics2D g2) { // draws the score, level, and lives at the top of the screen
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
        g2.drawString("LIVES", 680, 20);
 
        int heartX = 680;
        for (int i = 0; i < 3; i++) {
            Color heartColor;
            if (i >= lives) { // changes heart color based on how many lives the player has left
                heartColor = new Color(60, 60, 60);
            } else {
                heartColor = new Color(255, 60, 80);
            }
            drawHeart(g2, heartX + i * 30, 26, 14, heartColor);
        }
                g2.setColor(new Color(0, 255, 100, 40));
                g2.drawLine(0, 570, 800, 570);
            }
 
    void drawHeart(Graphics2D g2, int cx, int cy, int size, Color c) { // creates the heart
        g2.setColor(c);
        int s = size / 4;
        g2.fillRect(cx + s, cy, s * 2, s);
        g2.fillRect(cx + s * 3, cy, s * 2, s);
        g2.fillRect(cx, cy + s, s * 6, s * 2);
        g2.fillRect(cx + s, cy + s * 3, s * 4, s);
        g2.fillRect(cx + s * 2, cy + s * 4, s * 2, s);
    }
 
    void drawOverlay(Graphics2D g2, String title, Color titleColor, String sub1, String sub2) { // draws the game over and win screens, which includes the title, score, and restart instructions
        g2.setColor(new Color(0, 0, 0, 200)); // dims background
        g2.fillRect(0, 0, 800, 600);
 
        int cw = 480, ch = 180;
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
        g2.setColor(new Color(200, 200, 200));
        g2.drawString(sub2, cx + (cw - fm.stringWidth(sub2)) / 2, cy + 155);

        int bw = 220, bh = 40; // exit button
        int bx = cx + (cw - bw) / 2, by = cy + 200;
        g2.setColor(Color.BLACK);
        g2.fillRect(bx, by, bw, bh);
        g2.setColor(new Color(255, 80, 80));
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(bx, by, bw, bh);
        g2.setColor(Color.WHITE);
        g2.setFont(retroSmall);
        fm = g2.getFontMetrics();
        String btnText = "BACK TO MENU";
        g2.drawString(btnText, bx + (bw - fm.stringWidth(btnText)) / 2, by + (bh + fm.getAscent() - fm.getDescent()) / 2);

        menu = new Rectangle(bx, by, bw, bh);
    }

    @Override
    public void keyPressed(KeyEvent e) { // checks player input
        if (e.getKeyCode() == KeyEvent.VK_LEFT)  player.movingLeft  = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.movingRight = true;
        if (e.getKeyCode() == KeyEvent.VK_UP && gameState.equals("playing")) {
            bullets.add(new Bullet(player.x + 18, player.y, -8));
        }
        if (e.getKeyCode() == KeyEvent.VK_R && !gameState.equals("playing")) {
            restartGame();
        }
    }
 
    @Override
    public void keyReleased(KeyEvent e) { // stops player movement when keys are released
        if (e.getKeyCode() == KeyEvent.VK_LEFT)  player.movingLeft  = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.movingRight = false;
    }
 
    @Override 
    public void keyTyped(KeyEvent e) {} // not used but required by KeyListener interface
 
    @Override
    public void mouseClicked(MouseEvent e) { // allows player to click the back to menu button on the game over and win screens
        if (gameState.equals("playing")) { // if the game is still going, clicking will also shoot a bullet, this is intentional to add a bit of interactivity to the game while the player is waiting for the next level to start
            bullets.add(new Bullet(player.x + 18, player.y, -8));
        } else if (menu != null && menu.contains(e.getPoint())) { // checks if the back to menu button is clicked
            SwingUtilities.getWindowAncestor(this).dispose();
            PixelPurge.launchStart(currentUser);
        }
    }
 
    void saveHighScore(){ // saves the player's high score to Registration.txt, checks if the player already has a high score and only updates it if the new score is higher
        try{
            ArrayList<String> user = new ArrayList<>();
            try (Scanner scanner = Prompt.getInputScanner()) { // checks scanner
                while (scanner.hasNextLine()) {
                    user.add(scanner.nextLine());
                }
            }
            for (int i = 0; i + 2 < user.size(); i += 3) {
                if (user.get(i).trim().equals(currentUser)) {
                    int highScore = 0;
                    try { highScore = Integer.parseInt(user.get(i + 2).trim()); } catch (NumberFormatException ex) {}
                    if (score > highScore) {
                        user.set(i + 2, String.valueOf(score));
                    }
                    break;
                }
            }
            try (PrintWriter s = new PrintWriter(new FileWriter("Registration.txt", false))) {
                for (String a : user) {
                    s.println(a);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    void restartGame() { // once the game is over, resets all values to restart the game
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
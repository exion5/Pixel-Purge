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
    
    Font retro = null;
    Timer timer;

    GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        
        try {
            retro = Font.createFont(Font.TRUETYPE_FONT, new File("PressStart2P-Regular.ttf"))
                        .deriveFont(Font.BOLD, 20f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            retro = new Font("Arial", Font.BOLD, 20); // fallback font
        }


        player = new Player(380, 520);

        // Create enemies
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 8; col++) {
                enemies.add(new Enemy(50 + col * 60, 50 + row * 40));
            }
        }

        // Create shield blocks
        for (int i = 0; i < 4; i++) {
            int baseX = 150 + i * 150;
            for (int j = 0; j < 5; j++) {
                shields.add(new ShieldBlock(baseX + j * 20, 450));
            }
        }

        timer = new Timer(16, this);
        timer.start();

        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    void updateGame() {
        player.update();
        
        if (!enemies.isEmpty() && Math.random() < 0.02) { // 2% chance per frame
            int i = (int)(Math.random() * enemies.size()); // picks a random enemy
            Enemy shooting = enemies.get(i);
            bullets.add(new Bullet(shooting.x + 15, shooting.y + 20, 5)); // adds bullet to enemy position
        }

        // Update enemies
        boolean changeDirection = false;
        for (Enemy enemy : enemies) {
            enemy.update();
            if (enemy.x < 0 || enemy.x > 760) changeDirection = true;
        }

        // Make enemies bounce + move down
        if (changeDirection) {
            for (Enemy enemy : enemies) {
                enemy.dx *= -1;
                enemy.y += 20;
            }
        }

        for (Bullet b : bullets) b.update();
        for (Powerup p : powerup) p.update();
    
        checkCollisions();
        
        bullets.removeIf(b -> !b.alive);
        enemies.removeIf(e -> !e.alive);
        shields.removeIf(s -> !s.alive);
        powerup.removeIf(p -> !p.alive);
    }

    void checkCollisions() {
        Rectangle pr = player.getBounds();
        ArrayList<Powerup> powerupsToSpawn = new ArrayList<>();
    
        for (Bullet b : bullets) {
            Rectangle br = b.getBounds();
            
            if (b.dy < 0) { // checks if player hits enemy
                for (Enemy e : enemies) {
                    if (br.intersects(e.getBounds())) {
                        e.alive = false;
                        b.alive = false;
                        score += 100;
                        
                        if (Math.random() < 0.1) {
                            double p = Math.random();
                            String power = "";
                            if (p < 0.5){
                                power = "Shield";
                            } else if (p >= 0.5){
                                power = "Life";
                            }
                            powerup.add(new Powerup(e.x, e.y, power));
                        }
                    }
                }
            }
    
            if (b.dy > 0) { // checks if enemy hits player
                if (br.intersects(pr)) {
                    b.alive = false;
                    lives--;
                    
                    player.x = 100;
                    player.y = 520;
                    
                    if (lives <= 0){ // if you lose all 3 lives, you lose
                        timer.stop();
                    }
                }
            }
    
            for (ShieldBlock s : shields) { // check if bullet hits shield
                if (br.intersects(s.getBounds())) {
                    s.health--;
                    b.alive = false;
                    if (s.health <= 0) s.alive = false;
                }
            }
        }
        
        for (Powerup p : powerup){
            if (p.getBounds().intersects(player.getBounds())) {
                p.alive = false;
                
                if (p.type.equals("Life")) { // adds a life
                    lives++;
                } else if (p.type.equals("Shield")) { // restores shields
                    shields.clear();
                    for (int i = 0; i < 4; i++) {
                        int baseX = 150 + i * 150;
                        for (int j = 0; j < 5; j++) {
                            shields.add(new ShieldBlock(baseX + j * 20, 450));
                        }
                    }
                }
            }
        }
        powerup.addAll(powerupsToSpawn);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        player.draw(g2);
        
        for (int i = 0; i < enemies.size(); i++) enemies.get(i).draw(g2);
        for (int i = 0; i < bullets.size(); i++) bullets.get(i).draw(g2);
        for (int i = 0; i < shields.size(); i++) shields.get(i).draw(g2);
        for (int i = 0; i < powerup.size(); i++) powerup.get(i).draw(g2);

        g2.setColor(Color.WHITE);
        g2.setFont(retro.deriveFont(Font.BOLD, 20f));
        g2.drawString("Score: " + score, 20, 30);
        g2.drawString("Lives: " + lives, 600, 30);
    }

    // -------------------------
    // INPUT HANDLING
    // -------------------------
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) player.movingLeft = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.movingRight = true;
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            bullets.add(new Bullet(player.x + 18, player.y, -8));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) player.movingLeft = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.movingRight = false;
    }

    @Override public void keyTyped(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        bullets.add(new Bullet(player.x + 18, player.y, -8));
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
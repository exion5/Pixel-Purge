import java.awt.*;
 
class Player extends GameObject {
    int speed = 6;
    boolean movingLeft = false;
    boolean movingRight = false;
 
    // Engine flicker
    private int flickerTick = 0;
 
    Player(int x, int y) {
        super(x, y, 40, 20);
    }
 
    @Override
    void update() {
        if (movingLeft  && x > 0)   x -= speed;
        if (movingRight && x < 760) x += speed;
        flickerTick = (flickerTick + 1) % 6;
    }
 
    @Override
    void draw(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 
        // Engine exhaust flicker
        Color exhaustColor = (flickerTick < 3)
            ? new Color(255, 140, 0)
            : new Color(255, 60, 0);
        g.setColor(exhaustColor);
        g.fillRect(x + 14, y + 20, 5, 6 - flickerTick);
        g.fillRect(x + 22, y + 20, 5, 4 + flickerTick % 3);
 
        // Ship body - pixel art cannon shape
        // Base platform
        g.setColor(new Color(0, 200, 255));
        g.fillRect(x, y + 12, 40, 8);
 
        // Mid section
        g.setColor(new Color(0, 160, 220));
        g.fillRect(x + 8, y + 6, 24, 8);
 
        // Cannon tip
        g.setColor(new Color(0, 220, 255));
        g.fillRect(x + 17, y, 6, 8);
 
        // Highlight line
        g.setColor(new Color(180, 240, 255, 120));
        g.fillRect(x + 2, y + 13, 36, 2);
 
        // Side wings accent
        g.setColor(new Color(0, 255, 200));
        g.fillRect(x,      y + 14, 6, 4);
        g.fillRect(x + 34, y + 14, 6, 4);
 
        // Glow under ship
        g.setColor(new Color(0, 200, 255, 25));
        g.fillOval(x - 4, y + 18, 48, 10);
    }
}
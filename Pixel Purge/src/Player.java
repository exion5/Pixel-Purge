import java.awt.*;

class Player extends GameObject {
    int speed = 6;
    boolean movingLeft = false;
    boolean movingRight = false;
    private int flickerTick = 0; 

    private final Color orangeExhaust = new Color(255, 140, 0);
    private final Color redExhaust = new Color(255, 60, 0);
    private final Color lightBlue = new Color(0, 200, 255);
    private final Color darkBlue = new Color(0, 160, 220);
    private final Color brightBlue = new Color(0, 220, 255);
    private final Color neonGreen = new Color(0, 255, 200);
    private final Color whiteGlow = new Color(180, 240, 255, 120);
    private final Color shadowGlow = new Color(0, 200, 255, 25); 

    Player(int x, int y) {
        super(x, y, 40, 20); // sets player width to 40 and height to 20
    }

    @Override
    void update() { // movement
        if (movingLeft && x > 0) {
            x -= speed;
        }
        
        if (movingRight && x < 760) {
            x += speed;
        }
        
        flickerTick = (flickerTick + 1) % 6; // engine flickering
    }

    @Override
    void draw(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color exhaustColor;
        if (flickerTick < 3) {
            exhaustColor = orangeExhaust;
        } else {
            exhaustColor = redExhaust;
        }
        g.setColor(exhaustColor);
        g.fillRect(x + 14, y + 20, 5, 6 - flickerTick);
        g.fillRect(x + 22, y + 20, 5, 4 + (flickerTick % 3));

        g.setColor(lightBlue); // ship body
        g.fillRect(x, y + 12, 40, 8);
        g.setColor(darkBlue);
        g.fillRect(x + 8, y + 6, 24, 8);
        g.setColor(brightBlue);
        g.fillRect(x + 17, y, 6, 8);
        g.setColor(whiteGlow);
        g.fillRect(x + 2, y + 13, 36, 2);
        g.setColor(neonGreen);
        g.fillRect(x, y + 14, 6, 4);
        g.fillRect(x + 34, y + 14, 6, 4);
        g.setColor(shadowGlow);
        g.fillOval(x - 4, y + 18, 48, 10);
    }
}
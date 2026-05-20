import java.awt.*;

class Bullet extends GameObject { // creates bullets in the game for both player and enemies
    int dy;

    Bullet(int x, int y, int dy) {
        super(x, y, 5, 10);
        this.dy = dy;
    }

    @Override
    void update() {
        y += dy;
        if (y < 0 || y > 600) alive = false;
    }

    @Override
    void draw(Graphics2D g) {
        if (dy < 0) {
            g.setColor(Color.YELLOW); // Player bullet
        } else {
            g.setColor(Color.RED); // enemy bullet is red
        }
        g.fillRect(x, y, width, height);
    }
}
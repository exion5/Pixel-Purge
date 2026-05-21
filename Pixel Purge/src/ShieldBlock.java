import java.awt.*;

class ShieldBlock extends GameObject { // the shield in front of the player in game
    int health = 3;

    ShieldBlock(int x, int y) {
        super(x, y, 20, 20);
    }

    @Override
    void update() {}

    @Override
    void draw(Graphics2D g) { // the shield changes color as it takes damage, starting white and ending gray when destroyed
        if (health == 3){
            g.setColor(Color.WHITE);
        }
        else if (health == 2) {
            g.setColor(Color.LIGHT_GRAY);
        }
        else {
            g.setColor(Color.GRAY);
        }

        g.fillRect(x, y, width, height);
    }
}